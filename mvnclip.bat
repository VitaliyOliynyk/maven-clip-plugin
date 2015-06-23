:: example:
::
:: mvnclip "module1,module2,module3"
::
:: -DversionConfigureWay=[VERSION,LATEST]
::
mvn -o eu.vitaliy:maven-clip-plugin:0.1.2-SNAPSHOT:clip -DversionConfigureWay=VERSION -Dmodules=%1