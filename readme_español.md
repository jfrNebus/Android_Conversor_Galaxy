# Introducción

Hubo un momento en el pasado en el que tuve que trabajar con las central de alarmas de intrusión _Galaxy Dimension_, de la marca _HoneyWell_. El tiempo ha hecho que se me hayan olvidado ciertos conceptos e información de la misma, pues hace tiempo que ya no trabajo con estos equipos, pero aquí va el origen de esta aplicación.

Esta aplicación surge de la necesidad de mejorar el rendimiento de trabajo para hacer más liviana una actividad laboral específica. Cuando se trabaja con este tipo de sistemas, amenudo están conectados a una empresa de seguridad privada que supervisa las señales de la misma. Probar el correcto funcionamiento de la central puede resultar confuso cuando se trata de verificar si un un sensor concreto del sistema ha enviado su señal al centro de recepción de señales de la empresa de seguridad. Esto es debido a que la central de alarmas le asigna una identificación basada en un número, al que le puedes agregar un nombre, y el sistema de visualización de señales en directo de la empresa de seguridad le asigna otro número diferente al mismo dispositivo. Cuando revisas las señales que llegan a tu herramienta de trabajo, (móvil, tablet), las visualizas con la numeración que le da el sistema de la empresa de seguridad privada, y para poder verificar que dicha señal recibida corresponde al sensor que estás probando, necesitas una tabla de conversión que te muestre a qué numeración de la central de alarmas corresponde la numeración de la señal recibida en el software de la empresa. Esto genera una demora del trabajo notable cuando se están comprobando todos los sensores de un sistema completo, pues esa tabla se nos facilita en un archivo de texto sobre el que tienes que hacer scroll de forma constante. La situación más común era que, tras haber hecho saltar todos los sensores del sistema, tu compañero y tú os sentabais y mientras uno iba leyendo el histórico de señales, el otro iba marcando como "Probado" cada uno de los sensores en un excel, tras haber consultado la tabla.

* Ha saltado el sensor número 125.
* Se comprueba en la tabla la numeración del sensor 125 en la central de alarmas, que corrsponde con la zona 1135.
* Se comprueba en el Excel de información del cliente si ya se marcó como _Probada_ dicha zona.
* Se pasa al siguiente sensor en el histórico de señales.

Esta dinámica de trabajo genera un inconveniente notable. ¿Qué pasa si tu compañero y tú no estáis solos en la instalación mientras probáis todo el sistema? ocurrirá que habrá sensores que se activen más de una vez debido al paso del personal de la instalación, o de los clientes que acuden a dicha instalación. Esto hace que el historial de señales se sature de los reportes de aquellos sensores que estén en las zonas más transitadas de la instalación. En este caso, cuando se está revisando todo el historial y anotando los sensores probados con éxito, la frase más recurrente es "¿te he dicho ya la zona X?" donde X es el número de un sensor, (se denomina zona a cada uno de los sensores del sistema). Esto te obliga a perder tiempo revisando el documento para saber si ya le clasificaste como _probada_, porque es posible que no lo recuerdes si la instalación tiene muchos sensores.

Otro inconveniente notable es el de buscar cada zona en la tabla de conversión. Mas adelante en este documento se explicará cómo numera el sistema cada zona, por ahora solo decir que estos sistemas pueden contar con zonas cuya numeración va del 1001 al 1158, del 2001 al 2158, del 3001 al 3158, y del 4001 al 4158. Como la numeración depende de cómo se hayan cubierto las necesidades de seguridad de la instalación, en el historial de señales, tras haber probado el sistema, podemos encontrar reportes de una zona cuya numeración se encuentra  en el rango de 1001 a 1158, seguida de otra zona en el rango de 3001 a 3158, y a continuación otra en cualquier otro rango. Cuando se está revisando el historial, comprobar la tabla de conversión se traduce en un constante proceso de buscar X zona en el documento en su rango adecuado, lugo pasar X número de páginas para buscar la siguiente en su rango, etc.

Estas dos situaciones, a mi parecer, eran un sin sentido. En ese momento pensé que sería sencillo hacer una aplicación que te devuelva el número zona ya convertido, en función del tipo de central. Adicionalmente, podría añadirle un modo de funcionamiento que te reporte si ya introduciste una zona en concreto. De está forma, tendrías acceso a la conversión de la zona de forma rápida y podrías saber si ya comprobaste su estado con anterioridad. Esto eliminaría el constante scroll tanto en el archivo de control donde apuntamos cada zona que ha sido probada, como en la tabla de conversión.

<br>
<br>

## Funcionamiento 🧰

<br>

Vamos a empezar entendiendo cómo nombra la central de alarmas sus zonas. La central tiene clemas de conexión para 16 zonas, es decir, puedes conectar a la central 16 sensores. Si se necesitan más sensores se han de agragar ríos y placas expansoras. Se denomina río a la conexión por bus de diferentes elementos que envían y/o reciben información. Una plca expansora, una placa que te permite agregar X número de sensores, se comunica con la central a través de un río, un bus de comunicaciones, en este caso del tipo RS-485. Esta situación genera que, si necesitas 26 zonas en una central de tipo _Galaxy Classic_, (más adelante veremos los tipos de centrales Galaxy con las que se trabaja), vas a necesitar la central y un total de 2 expansores.

Cada expansor permite agregar un total de 8 zonas. Si a las 26 zonas del ejemplo anterior le restamos las 16 que permite agregar la propia central, nos quedan 10 zonas, por eso se necesitan 2 expansores. Se acomodan 8 de esas 10 zonas en un expansor, y las 2 últimas en el segundo.


