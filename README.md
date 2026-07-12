# Ticket Service — Evaluación Final Transversal (DOY0101 - Ingeniería DevOps)

Microservicio de gestión de tickets desarrollado en **Spring Boot** con **arquitectura
hexagonal**, con su ciclo de vida DevOps completo automatizado: control de versiones,
integración y despliegue continuo, seguridad, contenedores, orquestación, monitoreo y
cumplimiento normativo.

Este repositorio consolida el trabajo desarrollado durante el semestre en las
Evaluaciones Parciales 1, 2 y 3 de la asignatura, integrado y documentado como entrega
final.

---

## Tabla de contenidos

1. [Arquitectura del proyecto](#1-arquitectura-del-proyecto)
2. [Estrategia de ramificación](#2-estrategia-de-ramificación-ie1-ie2-ie3-ie5)
3. [Contenedores](#3-contenedores-ie6)
4. [Pipeline CI/CD](#4-pipeline-cicd-ie4-ie7-ie9)
5. [Análisis de código y seguridad](#5-análisis-de-código-y-seguridad-ie8-ie13)
6. [Despliegue y orquestación](#6-despliegue-y-orquestación-ie9-ie10)
7. [Monitoreo y logging](#7-monitoreo-y-logging-ie11-ie12)
8. [Uso de Inteligencia Artificial](#8-uso-de-inteligencia-artificial)
9. [Reflexiones individuales](#9-reflexiones-individuales)

---

## 1. Arquitectura del proyecto

- **Lenguaje/Framework:** Java 17, Spring Boot, Maven.
- **Patrón:** Arquitectura hexagonal (puertos y adaptadores), separando dominio,
  aplicación e infraestructura.
- **Persistencia:** MySQL, gestionada vía Docker Compose junto al servicio.
- **Equipo:** Dominique (dxminique) y Héctor (hectorpena95).

## 2. Estrategia de ramificación (IE1, IE2, IE3, IE5)

Se utiliza un modelo simplificado de **GitFlow**:

| Rama | Propósito |
|---|---|
| `main` | Código estable. Cada push aquí dispara el pipeline completo de build, seguridad, calidad, y despliegue a producción. |
| `develop` | Rama de integración. Las features se prueban acá antes de pasar a `main`. |
| `feature/*` | Nuevas funcionalidades. Ramifica desde `develop`, se integra vía Pull Request. |
| `hotfix/*` | Correcciones urgentes. Ramifica desde `main`, se integra vía PR a `main` y luego se sincroniza con `develop`. |
| `dependabot/*` | Ramas automáticas generadas por Dependabot para actualizar dependencias (JaCoCo, Sonar Maven Plugin, GitHub Actions). |

**Convención de commits:** `tipo: descripción breve` — ej. `feat:`, `fix:`, `chore:`,
`docs:`, `refactor:`. Esto facilita trazabilidad y generación de changelog.

**Flujo colaborativo demostrado:** el historial de commits y Pull Requests de este
repositorio evidencia el ciclo completo `feature → develop → main` y al menos un
`hotfix → main`, con revisión antes de cada merge.

## 3. Contenedores (IE6)

- `Dockerfile`: construye la imagen del ticket-service, empaquetando el `.jar` generado
  por Maven sobre una imagen base liviana de Java (Eclipse Temurin / JRE).
- `docker-compose.yml`: orquesta el servicio junto a su base de datos MySQL, definiendo
  variables de entorno, puertos y dependencias de arranque (`depends_on`).
- Imagen publicada en **Docker Hub** bajo la cuenta `dxminique`.

## 4. Pipeline CI/CD (IE4, IE7, IE9)

Definido en `.github/workflows/`, con las siguientes etapas:

1. **Build y test** — compila el proyecto y ejecuta pruebas unitarias, generando
   reporte de cobertura con **JaCoCo**.
2. **Seguridad (Snyk)** — escanea dependencias en busca de vulnerabilidades.
   Configurado con umbral crítico: si se detecta una vulnerabilidad crítica, **el
   pipeline se detiene** y no continúa a build de imagen ni despliegue.
3. **Calidad (SonarCloud)** — análisis estático de código con Quality Gate
   bloqueante (`sonar.qualitygate.wait=true`): si el código no cumple los criterios
   de calidad, el pipeline no avanza.
4. **Build y push de imagen Docker** — solo se ejecuta si seguridad y calidad
   pasaron correctamente. La imagen se etiqueta con el hash del commit para
   trazabilidad.
5. **Despliegue automático en EC2** — vía Docker Compose, ejecutado por un
   **self-hosted runner** instalado en la instancia (necesario porque AWS Academy
   restringe SSH/SSM tradicionales).
6. **Notificación** — reporta el resultado final del pipeline.

El pipeline se dispara en cada push a `develop` y en cada Pull Request hacia `main`,
garantizando que ningún cambio llegue a producción sin pasar por seguridad y calidad.

## 5. Análisis de código y seguridad (IE8, IE13)

- **SonarCloud** — organización `dxminique`, analiza cada PR a `main` en busca de
  code smells, duplicación, y vulnerabilidades de código.
- **Snyk** — analiza dependencias Maven (`pom.xml`) en busca de CVEs conocidos.
- **Dependabot** — mantiene actualizadas automáticamente las dependencias del
  proyecto (visible en las ramas `dependabot/*` de este repo).
- **Branch protection rules** en `main` — requiere que los checks de SonarCloud y
  Snyk pasen, y que el cambio venga vía Pull Request, antes de permitir el merge.

## 6. Despliegue y orquestación (IE9, IE10)

- **Infraestructura:** AWS EC2 (Amazon Linux), dentro del entorno de AWS Academy.
- **Orquestación:** Docker Compose coordina el ticket-service junto con su base de
  datos MySQL, gestionando el orden de arranque y la red interna entre contenedores.
- **Trazabilidad:** cada imagen desplegada corresponde a un commit específico
  (tag = SHA del commit), permitiendo identificar exactamente qué versión del
  código está corriendo en producción en cualquier momento.
- **Verificación post-despliegue:** el pipeline valida automáticamente que el
  servicio responda correctamente (`/actuator/health`) antes de marcar el deploy
  como exitoso.

## 7. Monitoreo y logging (IE11, IE12)

- **CloudWatch Logs:** logging centralizado de la aplicación, permitiendo revisar
  el comportamiento del servicio y detectar errores sin acceder manualmente a la
  instancia EC2.
- **CloudWatch Alarm** (`alarma-cpu-ticket-service`): monitorea el uso de CPU de
  la instancia y envía notificación por correo (SNS) si se supera el umbral
  definido, permitiendo detectar sobrecarga o comportamiento anómalo.
- **Dashboard de métricas:** panel en CloudWatch con indicadores clave (uso de
  CPU, memoria, disponibilidad del servicio) que apoyan la toma de decisiones
  sobre escalado o intervención.

  *(Adjuntar captura del dashboard de CloudWatch aquí)*

## 8. Uso de Inteligencia Artificial

Se utilizó **Claude (Anthropic)** como apoyo para:
- Mejorar la redacción y estructura de esta documentación.
- Generar un borrador inicial del workflow de GitHub Actions, revisado y ajustado
  manualmente para corregir el comportamiento del gate de seguridad (Snyk) y
  adaptarlo a la infraestructura real del proyecto (self-hosted runner en EC2).
- Resolver errores puntuales de configuración (permisos IAM en AWS Academy,
  timeouts de conexión SSH, versión de dependencias).

Todas las decisiones de arquitectura, la implementación final del código y las
conclusiones del proyecto son propias del equipo. Referencia de uso ético de IA:
https://bibliotecas.duoc.cl/ia

## 9. Reflexiones individuales

### Dominique

*Aprendi a como integrar seguridad a mi repositorio con las herramientas entregadas en clases como Snyk, jacoco y sonarQube, tambien la orquestacion de docker coordinando la comunicación entre el microservicio y la base de datos MySQL mediante healthchecks. y a como funciona un pipeline CI/CD.*

### Héctor

*Durante el desarrollo de esta evaluación, pude consolidar el impacto de adoptar una cultura DevOps de extremo a extremo. Destaco especialmente el aprendizaje práctico en el diseño e implementación del pipeline CI/CD con GitHub Actions, integrando umbrales de calidad y seguridad bloqueantes (Snyk y SonarCloud) que garantizan un código robusto antes del despliegue. Asimismo, la experiencia de gestionar el despliegue automatizado mediante Docker Compose y un self-hosted runner en AWS EC2 me permitió comprender los desafíos reales de infraestructura y la importancia del monitoreo continuo con CloudWatch para asegurar la alta disponibilidad del servicio.*
