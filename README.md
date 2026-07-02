# Ticket Service — EP2 DOY0101_ID

Microservicio de gestión de tickets con pipeline CI/CD completo implementado en GitHub Actions, usando contenedores Docker, análisis de seguridad y despliegue automatizado en AWS.

---

## Arquitectura del pipeline

```
Push/PR → Build & Tests → Snyk + SonarCloud → Docker Build → ECR Push → Deploy EC2
```

### Etapas del pipeline

| Etapa | Herramienta | Propósito |
|---|---|---|
| Build y Tests | Maven + JaCoCo | Compilación y cobertura ≥60% |
| Seguridad | Snyk | Escaneo de dependencias vulnerables |
| Calidad de código | SonarCloud | Análisis estático, code smells, bugs |
| Dependencias | Dependabot | PRs automáticos con actualizaciones |
| Contenedor | Docker multi-stage | Imagen optimizada y segura |
| Registro | AWS ECR | Almacenamiento de imágenes Docker |
| Deploy | EC2 + Docker Compose | Entorno cloud simulado |

---

## Trazabilidad

Cada commit genera una imagen Docker etiquetada con el SHA del commit (`${{ github.sha }}`), lo que permite identificar exactamente qué versión del código está desplegada en producción.

El pipeline **bloquea el deploy** si:
- Los tests fallan
- Snyk detecta vulnerabilidades de severidad `high` o superior
- SonarCloud reporta el Quality Gate como fallido

---

## Orquestación de contenedores (Docker Compose)

```yaml
services:
  mysql   → Base de datos con healthcheck
  ticket  → Microservicio (depende de mysql healthy)
```

Levantamiento local:
```bash
docker compose up -d
```

---

## Secrets requeridos en GitHub

| Secret | Descripción |
|---|---|
| `SNYK_TOKEN` | Token de autenticación Snyk |
| `SONAR_TOKEN` | Token de SonarCloud |
| `SONAR_ORGANIZATION` | Organización en SonarCloud |
| `AWS_ACCESS_KEY_ID` | Credenciales AWS |
| `AWS_SECRET_ACCESS_KEY` | Credenciales AWS |
| `EC2_HOST` | IP pública de la instancia EC2 |
| `EC2_USER` | Usuario SSH (ej: `ec2-user`) |
| `EC2_SSH_KEY` | Clave privada SSH |
| `ECR_REGISTRY` | URL del registro ECR |

---

## Garantía de calidad

- **Pruebas unitarias** con JUnit 5 + Mockito (sin dependencia de base de datos real)
- **Cobertura mínima** del 60% validada por JaCoCo en cada ejecución
- **Análisis de dependencias** automático cada lunes vía Dependabot
- **Análisis de código** en cada push vía SonarCloud

---
## Reflexiones individuales
**DOMINIQUE COFRE**
Aprendi a como integrar seguridad a mi repositorio con las herramientas entregadas en clases como Snyk, jacoco y sonarQube, tambien la orquestacion de docker coordinando la comunicación entre el microservicio y la base de datos MySQL mediante healthchecks. y a como funciona un pipeline CI/CD.

tuve algunos problemas con las credenciales de AWS ya que expiran y debia cambiarlas siempre en los secrets del github, me confundi de EC2 al realizar la prueba asi que tuve que lo que me obligó a instalar Docker y configurar las credenciales de AWS CLI nuevamente en la instancia correcta. Otro problema fue con el archivo mvnw que no tenía permisos de ejecución en el repositorio, lo que impedía que el pipeline compilara el proyecto.

Como mejora futura, me gustaría implementar orquestación con Kubernetes, que actualmente estoy aprendiendo tambien como poder actualizar las credenciales sin tener que agregarlas manualmente.

**HECTOR PEÑA**
Qué aprendiste del proyecto: Aprendí a estructurar un pipeline de CI/CD automatizado, integrando pruebas, análisis de calidad (SonarCloud), seguridad (Snyk) y despliegue en AWS.
Qué parte hiciste tú: Me enfoqué en el código del microservicio, el desarrollo de pruebas unitarias con JUnit 5/Mockito para superar el 60% de cobertura y en la configuración de Docker.
Qué dificultades tuviste: El mayor reto fue corregir los bugs y code smells que detectaba SonarCloud, ya que el Quality Gate fallido bloqueaba el despliegue a la EC2.
Qué mejorarías: Subiría la cobertura de código a un 80% con pruebas de integración y configuraría alertas automáticas en Discord/Slack si el pipeline llega a fallar.

## Uso de IA

Este proyecto utilizó Claude (Anthropic) como apoyo para la generación de archivos de configuración (Dockerfile, docker-compose.yml, workflows). Todas las decisiones técnicas, ajustes y validaciones fueron realizadas por el equipo. Citado según política DuocUC: https://bibliotecas.duoc.cl/ia
