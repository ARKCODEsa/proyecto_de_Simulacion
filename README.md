# Simulador de Inventarios para una Panadería - Optimización de la Gestión de Stock

**Autor:** Jorge Jefferson Ortiz Caicedo  
**Fecha de Entrega:** 25/12/2024

---

## Descripción General
Este proyecto consiste en el desarrollo de un **Simulador de Inventarios** diseñado para optimizar la gestión de stock en una panadería, aunque su aplicación puede extenderse a otros dominios con dinámicas similares. El simulador aborda problemas clave como el **sobreinventario** y el **subinventario**, reduciendo costos y asegurando un flujo adecuado de productos mediante estrategias basadas en simulación y cálculos predictivos.

## Características Principales
1. **Gestión de Productos**:
    - Agregar, editar y eliminar productos en el inventario.
    - Configuración de parámetros básicos como demanda diaria, niveles iniciales y tiempo de reorden.

2. **Simulación Predictiva**:
    - Ajuste de variables globales: inventario inicial, demanda promedio, tiempo de simulación.
    - Ejecución de simulaciones automáticas para calcular consumo de inventario y reabastecimientos.

3. **Visualización y Reportes**:
    - Gráficos en tiempo real que representan los niveles de inventario.
    - Tablas detalladas con registros como demanda diaria y eventos de reorden.

4. **Optimización con EOQ**
    - Cálculo automático de la cantidad económica de pedido utilizando la fórmula de Wilson:
      ```
      EOQ = √((2 * D * S) / H)
      D = Demanda anual estimada
      S = Costo por pedido
      H = Costo anual de almacenamiento por unidad
      ```

## Componentes Técnicos
- **Tecnologías Utilizadas**:
    - JavaFX: Creación de la interfaz gráfica interactiva.
    - FXML: Para modularizar las vistas y controlarlas de forma eficiente.

- **Clases Principales**:
    - **Inventario**: Gestiona los productos y realiza cálculos estadísticos.
    - **Producto**: Define propiedades del artículo como cantidad inicial y demanda diaria.
    - **InventarioRecord**: Registra cada evento de inventario en el ciclo simulado.

- **Cálculos de Simulación**:
    - Generación aleatoria de demanda utilizando una distribución normal.
    - Optimización de los niveles de inventario a partir del **EOQ** y puntos de reorden.

## Instrucciones de Uso
1. Ejecutar la aplicación desde el entorno JavaFX.
2. Configurar los parámetros iniciales del inventario, como:
    - Cantidad inicial de productos.
    - Demanda diaria promedio.
    - Tiempo y punto de reabastecimiento.
3. Inicie la simulación para evaluar el desempeño del inventario a lo largo del tiempo.
4. Analizar los resultados mediante gráficos y tablas generadas automáticamente en la interfaz.

## Resultados Esperados
- Evitar situaciones de sobreinventario o desabastecimiento mediante un correcto cálculo del punto de reorden.
- Identificar tendencias críticas en el comportamiento del inventario según escenarios personalizados.
- Generar reportes detallados que faciliten el análisis a nivel operativo.

## Conclusiones
- **Eficiencia**: El simulador permite gestionar dinámicas de inventarios complejas, ajustándose a las necesidades del negocio.
- **Flexibilidad**: El diseño modular lo hace adaptable a múltiples contextos y añade potencial escalabilidad.
- **Aplicación**: Puede ser utilizado en diferentes industrias como supermercados, farmacias y almacenes industriales.

## Próximos Pasos / Mejoras
1. Incorporar un historial de simulaciones para detectar tendencias a largo plazo.
2. Permitir descarga de reportes automáticos en formatos como PDF o Excel.
3. Ampliar el simulador a escenarios con mayor variabilidad de demanda y más productos.

## Requisitos del Sistema
- **Java Development Kit (JDK):** 17+
- **JavaFX SDK** instalado y configurado.

## Enlaces Importantes
- **Código Fuente:** Disponible en el repositorio de GitHub.
- **Video Explicativo:** Link alojado en MEGA.

## Contacto
Para consultas adicionales, contactar a:
- **arqjhefferson@hotmail.com**
- **jjortizc@ube.edu.ec**  