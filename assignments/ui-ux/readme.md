---
title: ""
subtitle: ""
author: ""
abstract: ""
titlepage: True
toc: false
citeproc: true
numbersections: true
bibliography: /home/marcos/.zotero/export.bib
csl: /home/marcos/Zotero/styles/the-lancet.csl
variable:
    - papersize=a4paper
    - classoption=twocolumn % two column layout
cssclasses:
    - academic
    - twocolumn
header-includes: |
    \usepackage{header}
---

\includepdf{./static/cover.pdf}
\pagebreak
\tableofcontents
\pagebreak

# Introducción

La importancia de un buen desarrollo ui/ux no puede ser exagerada. Está comprobado que un sitio estético junto a una buena experiencia de usuario es uno de los factores de éxito en las aplicaciones modernas. Entre otros estadísticas, se mostró que:

-   El 88% de los usuarios no volverán a un sitio/aplicación si tuvieron una mala experiencia con el mismo.
-   La tasa por la cual un usuario toma acción(conversion rate) puede llegar a impulsarse hasta un 200%.
-   El 94% de los usuarios que tuvo una buena experiencia puede ser más influenciable que una gran campaña de marketing.
-   Invertir en una buena interfaz tiene un retorno de $2 a $100 por dólar invertido.

En este documento, se analizará la página del Banco Patagonia y se verán aquellos puntos donde su diseño podría ser mejorado.

\pagebreak

# El caso Banco Patagonia

## Landing Page

Apenas se ingresa a la página uno se encuentra con lo siguiente:

![Patagonia landing](Patagonia%20landing.png){ width=50% }

Inmediatamente, se nota lo complejo de la barra de navegación. Se está ante una doble _top-bar navigation_ con demasiados links, cuando a la gran mayoría de los usuarios solo les importa:

-   Personas.
-   Empresas.
-   Ayuda.
-   Ingresar al banco.

Al usuario no le gusta pensar, por tanto es importante asegurarse de diseñar de forma simple, priorizando lo que le importa al usuario, y siguiendo los patrones de navegación e interacción establecidos de manera tal que pueda desenvolverse en el sitio de forma intuitiva.

En este caso, se puede solucionar "escondiendo" los demás links a través de una _hamburger navigation_ o, mejor aún, desviados a otra secciones como el _footer_. De esta manera, se limpiaría bastante la navegación, haciéndola más sencillo y clara, con lo cual no se abrumara al usuario.

Además, el botón para _"ingresar al ebank"_ es una foto. Lo cierto es que resulta confuso, lo mejor sería utilizar un botón convencional el cual sea **consistente** con el resto de los botones y **alinearlo** en la misma jerarquía que los botones de navegación.

Un ejemplo de una navegación bien implementada sería el caso del Banco Galicia:

![Galicia landing](Galicia%20landing.png){width=50%}

![Galicia footer](Galicia%20footer.png){width=50%}

En este caso, se puede ver como la navegación está claramente simplificada, mostrando solamente aquellos links más visitados e importantes y apartando los demás en el _footer_.

Por otro lado, en cuanto a la _ui_, el diseño está claramente anticuado.

-   Las imágenes son de baja calidad, en pantallas grandes se nota el reescalado y su baja resolución.
-   Los botones están estirados en toda la pantalla y poseen efectos de animación raros(cambian entre color verde-azul), lo que no queda claro si es un botón o una publicidad.
-   Ciertos botones como el de _"solicitar turno"_ poseen un color que no es armonioso con el resto del sitio.

Al ser este el home del banco, debería ser aprovechado para mostrar todos los beneficios del mismo a través de varias secciones, y no simplemente una galería de 5 fotos de baja calidad. De esta manera, es más probable que se genere una buena impresión en aquellos primeros usuarios, aumentando así también la probabilidad de que se registren.

Un ejemplo de una landing bien diseñada es el caso de [brubank](https://www.brubank.com/).

![Brubank landing](Brubank%20landing.png){width=50%}

### Versión Mobile

Por otro lado su versión para pantallas pequeñas se ve de la siguiente manera:

![Patagonia mobile](Patagonia%20mobile.png){width=50%}

En este caso, aplica todo lo anterior, solo que la navegación cambia. Si bien en este caso se aplica la _hamburger navigation_(lo usual), nuevamente, hay varios errores graves de diseño en la navegación, veamos:

-   El botón más importante(ingresar al eBank), esta en la esquina inferior derecha, un decisión completamente inusual. Lo más usual, y por lo tanto lo que el usuario espera, es que se ubique un icono en la barra superior. En este caso podría ser en la parte superior-derecha.
-   La _bottom-tabs navigation_ esta aplicada de una manera anti-intuitiva. Usualmente, este tipo de navegación es utilizada en **aplicaciones**. Uno esperaría que cada botón nos cambie de vista, sin embargo, en este caso nos redirige(nos abre otra pestaña) a otra ruta.

Un buen ejemplo de navegación en móviles es el sitio del Banco BBVA:

![BBVA mobile](BBVA%20mobile.png){width=50%}

## Inicio de sesión

Por último analizaremos el inicio de sesión en el banco. El mismo se ve de la siguiente manera:

![Patagonia inicio](Patagonia%20inicio.png){width=50%}

Claramente, parece que volvimos a un sitio de los 90. Si bien todas las páginas del Banco Patagonia quedaron algo anticuadas, es inaceptable que un sitio en pleno 2024 se vea de esa forma. Lo peor de todo es que hay una imagen de aviso de estafa. La estafa es el sitio mismo, claramente no genera ningún tipo de confianza ni alienta a registrarse. De hecho, genera desconfianza e inseguridad, incluso da la sensación de como si fuese un sitio fácilmente hackeable. Desde una perspectiva de sistemas, da la impresión de que la aplicación fue desarrollada puramente por desarrolladores back-end, sin ningún diseñador, en los años 2000 en php 3 y todavía tiene todas las inseguridades de aquel entonces.

La solución sería sencilla

-   Utilizar inputs más grandes.
-   Utilizar una tipografía más equilibrada y aumentar su tamaño.
-   Utilizar botones más grandes
-   Espaciar los elementos, ajustar sus tamaño y jugar con los colores para declarar una jerarquía que guié al usuario a la hora de interactuar y NO LO HAGA PENSAR(Don't make me think).

Un ejemplo de un inicio claro, confiable es el caso del Banco Galicia

![Galicia inicio](Galicia%20inicio.png){width=50%}

En este caso, se aplican todos los principios heurísticos. Ej: se le da visibilidad del estado de la forma al usuario, dándole a saber los estados de carga, los errores y su motivo, etc.

\tableofcontents

# Conclusión

En conclusión, el análisis detallado de la página del Banco Patagonia revela diversas deficiencias en su diseño UI/UX que afectan negativamente la experiencia del usuario. Desde una navegación complicada y una interfaz visual anticuada, hasta problemas de usabilidad y falta de coherencia en el diseño.

Sin lugar a dudas, el Banco Patagonia podría hacer buen uso de una contratación masiva de diseñadores y de un manual de aplicación de principios de diseño modernos y centrados en el usuario...
