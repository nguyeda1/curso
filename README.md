# Lokální spuštění
## Instalace a nasazení
Pro lokální spuštění je třeba mít nainstalovaný PostgreSQL, Maven a JDK. Stažení PostgreSQL https://www.postgresql.org/download/. Instalace Mavenu a JDK https://howtodoinjava.com/maven/how-to-install-maven-on-windows/.

1. **Stáhnout projekt curso z github repozitáře https://github.com/nguyeda1/curso**

2. **Vytvoření lokální databáze pomocí psql skriptu:**

    
    CREATE USER "curso−dev" WITH PASSWORD ’xxx’;
    CREATE DATABASE "curso−dev" WITH ENCODING=’UTF8’ OWNER="curso−dev" CONNECTION LIMIT=−1;
    

3. **Stažení aplikačního serveru Payara https://www.payara.fish/software/downloads/**

4. **Ve složce staženáPayara/glassfish/config/domain1/config je soubor domain.xml, který upravíte.**

K node security-service přidejte následující:
      
      <auth−realm classname="com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm" name="curso−dev">
        <property name="jaas−context" value="jdbcRealm"></property>
        <property name="charset" value="UTF−8"></property>
        <property name="encoding" value="Hex"></property>
        <property name="digest−algorithm" value="MD5"></property>
        <property name="digestrealm−password−enc−algorithm" value="MD5"></property>
        <property name="datasource−jndi" value="jdbc/dev/curso"></property>
        <property name="user−table" value="sysuser"></property>
        <property name="group−table" value="user_group"></property>
        <property name="user−name−column" value="username"></property>
        <property name="password−column" value="password"></property>
        <property name="group−name−column" value="groupname"></property>
        <property name="group−table−user−name−column" value="username"></property>
      </auth−realm>
      
      
K node resource přidejte následující:      
        
        <jdbc−connection−pool 
          datasource−classname="org.postgresql.ds.PGConnectionPoolDataSource" 
          name="pg−curso−dev" res−type="javax.sql.ConnectionPoolDataSource">
            <property name="PortNumber" value="5432"></property>
            <property name="Password" value="xxx"></property>
            <property name="ServerName" value="localhost"></property>
            <property name="DatabaseName" value="curso−dev"></property>
            <property name="User" value="curso−dev"></property>
          </jdbc−connection−pool>
        <jdbc−resource pool−name="pg−curso−dev" jndi−name="jdbc/dev/curso"></jdbc−resource>
        
          
K node server přidejte následující:

      
      <resource−ref ref="jdbc/dev/curso"></resource−ref>
    
      
5. **Ve složce curso spusťte příkazový řádek a v něm příkaz:**
    ```
    mvn clean install
    ```
      

6. **Ve složce staženáPayara/bin spusťte příkazový řádek a v něm příkazy (jeden po druhém):**
    ```
    ./asadmin start−domain
    ./asadmin deploy curso/curso-ear/target/curso-dev-ear-1.0-SNAPSHOT.ear
    ```

## Přihlášení
Mobilní aplikace by měla být dostupná z prohlížeče na http://localhost:8080/tasks/cursodev/#/.
Pro optimální zobrazení spustit v mobilním prohlížeči nebo na normálním prohlížeči pomocí vývojářských nástrojů s možností zobrazení pro mobilní zařízení.
**Username: operative, Password: 12345**

Manažerská aplikace by měla být dostupná z prohlížeče na http://localhost:8080/cursodev/app. 
**Username: admin, Password: 12345**.
