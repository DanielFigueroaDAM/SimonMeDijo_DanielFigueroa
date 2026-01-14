---
agent: 'agent'
description: 'Crear un nuevo archivo de plan de implementación para nuevas características, refactorización de código existente o actualización de paquetes, diseño, arquitectura o infraestructura.'
tools: ['changes', 'search/codebase', 'edit/editFiles', 'extensions', 'fetch', 'githubRepo', 'openSimpleBrowser', 'problems', 'runTasks', 'search', 'search/searchResults', 'runCommands/terminalLastCommand', 'runCommands/terminalSelection', 'testFailure', 'usages', 'vscodeAPI']
---
# Crear Plan de Implementación

## Directiva Principal

Tu objetivo es crear un nuevo archivo de plan de implementación para `${input:PlanPurpose}`. Tu salida debe ser legible por máquina, determinista y estructurada para la ejecución autónoma por otros sistemas de IA o humanos.
Usa el idioma "Español" para redactar el plan.

## Contexto de Ejecución

Este prompt está diseñado para la comunicación de IA a IA y el procesamiento automatizado. Todas las instrucciones deben interpretarse literalmente y ejecutarse sistemáticamente sin interpretación o aclaración humana.

## Requisitos Fundamentales

- Generar planes de implementación que sean completamente ejecutables por agentes de IA o humanos.
- Usar un lenguaje determinista sin ambigüedades.
- Estructurar todo el contenido para el análisis y la ejecución automatizados.
- Asegurar la autocontención completa sin dependencias externas para la comprensión.

## Requisitos de Estructura del Plan

Los planes deben consistir en fases discretas y atómicas que contengan tareas ejecutables. Cada fase debe poder ser procesada de forma independiente por agentes de IA o humanos sin dependencias entre fases, a menos que se declaren explícitamente.

## Arquitectura de Fases

- Cada fase debe tener criterios de finalización medibles.
- Las tareas dentro de las fases deben ser ejecutables en paralelo, a menos que se especifiquen dependencias.
- Todas las descripciones de tareas deben incluir rutas de archivo específicas, nombres de funciones y detalles de implementación exactos.
- Ninguna tarea debe requerir interpretación o toma de decisiones humanas.

## Estándares de Implementación Optimizados para IA

- Usa un lenguaje explícito y sin ambigüedades que no requiera interpretación.
- Estructura todo el contenido en formatos analizables por máquina (tablas, listas, datos estructurados).
- Incluye rutas de archivo específicas, números de línea y referencias de código exactas cuando sea aplicable.
- Define explícitamente todas las variables, constantes y valores de configuración.
- Proporciona un contexto completo dentro de cada descripción de tarea.
- Usa prefijos estandarizados para todos los identificadores (REQ-, TASK-, etc.).
- Incluye criterios de validación que se puedan verificar automáticamente.

## Especificaciones del Archivo de Salida

- Guarda los archivos del plan de implementación en el directorio `/plan/`.
- Usa la convención de nomenclatura: `[proposito]-[componente]-[version].md`.
- Prefijos de propósito: `upgrade|refactor|feature|data|infrastructure|process|architecture|design`.
- Ejemplo: `upgrade-system-command-4.md`, `feature-auth-module-1.md`.
- El archivo debe ser un Markdown válido con una estructura de front matter adecuada.

## Estructura de Plantilla Obligatoria

Todos los planes de implementación deben adherirse estrictamente a la siguiente plantilla. Cada sección es obligatoria y debe completarse con contenido específico y procesable. Los agentes de IA deben validar el cumplimiento de la plantilla antes de la ejecución.

## Reglas de Validación de Plantilla

- Todos los campos del front matter deben estar presentes y formateados correctamente.
- Todos los encabezados de sección deben coincidir exactamente (sensible a mayúsculas).
- Todos los prefijos de identificador deben seguir el formato especificado.
- Las tablas deben incluir todas las columnas requeridas.
- No debe quedar ningún texto de marcador de posición en la salida final.

## Estado

El estado del plan de implementación debe definirse claramente en el front matter y debe reflejar el estado actual del plan. El estado puede ser uno de los siguientes (color del badge entre paréntesis): `Completado` (badge verde brillante), `En progreso` (badge amarillo), `Planificado` (badge azul), `Obsoleto` (badge rojo) o `En espera` (badge naranja). También debe mostrarse como un badge en la sección de introducción.

# Plan de Desarrollo: [Nombre del Proyecto]

![Estado: PLANIFICANDO](https://img.shields.io/badge/estado-PLANIFICANDO-ffaa00)
![Prioridad: ALTA](https://img.shields.io/badge/prioridad-ALTA-red)
![Esfuerzo: MEDIO](https://img.shields.io/badge/esfuerzo-MEDIO-yellow)

**Versión**: 1.0  
**Última actualización**: [Fecha]  
**Propietario**: [Tu nombre]

## 🎯 Objetivo Principal
[Describe en 1-2 oraciones QUÉ vas a construir y POR QUÉ es valioso]

**Ejemplo**: "Desarrollar una aplicación web para gestionar mis gastos personales que me permita visualizar patrones de gasto y establecer presupuestos mensuales de forma intuitiva."

## 📋 Contexto y Motivación

### ¿Por qué este proyecto?
- [ ] **Necesidad personal**: [Explica tu necesidad]
- [ ] **Aprendizaje**: [Qué tecnologías/qué conceptos quieres aprender]
- [ ] **Portafolio**: [Si aplica para mostrar habilidades]

### ¿Qué problema resuelve?
1. Problema actual: [Describe la situación actual]
2. Solución propuesta: [Cómo este proyecto lo mejora]

## 🎨 Alcance (Scope)

### ✅ INCLUIDO
- [ ] Feature 1: [Descripción breve]
- [ ] Feature 2: [Descripción breve]
- [ ] Feature 3: [Descripción breve]

### ❌ NO INCLUIDO (por ahora)
- [ ] Feature complejo que postergas
- [ ] Integración que no es esencial
- [ ] Optimización que puede venir después

## 🛠️ Stack Tecnológico

| Categoría | Tecnología Elegida | Justificación |
|-----------|-------------------|---------------|
| Frontend | React / Vue / Svelte | [Por qué esta elección] |
| Backend | Node.js / Python / Sin backend | [Razón] |
| Base de datos | SQLite / PostgreSQL / Firebase | [Ventajas para tu caso] |
| Estilos | Tailwind / CSS Modules | [Preferencia] |
| Hosting | Vercel / Netlify / GitHub Pages | [Costo/facilidad] |

## 📅 Plan de Acción

### Fase 1: Prototipo Funcional (Semana 1)
**Objetivo**: Tener algo que funcione end-to-end

| Tarea | Estado | Notas |
|-------|--------|-------|
| Setup del proyecto | ✅ | Usar vite/create-react-app |
| Diseño de la estructura base | 🔄 | Definir componentes principales |
| Implementar flujo básico | ⏳ | CRUD simple |
| Primer deploy | | Subir a hosting |

### Fase 2: Features Esenciales (Semana 2-3)
**Objetivo**: Implementar las funcionalidades clave

| Tarea | Prioridad | Estimación |
|-------|-----------|------------|
| [ ] Feature principal 1 | Alta | 2 días |
| [ ] Feature principal 2 | Alta | 1.5 días |
| [ ] Sistema de persistencia | Media | 1 día |
| [ ] UI/UX mejorado | Baja | 2 días |

### Fase 3: Pulido y Mejoras (Semana 4)
**Objetivo**: Hacerlo presentable y usable

| Tarea | Descripción | Depende de |
|-------|-------------|------------|
| [ ] Responsive design | Que funcione en móvil | Fase 2 |
| [ ] Manejo de errores | Feedback al usuario | Fase 2 |
| [ ] Optimizaciones | Performance básica | Fase 2 |
| [ ] Documentación | README y comentarios | Todo |

## 📊 Sistema de Seguimiento

### Tablero Kanban Personal