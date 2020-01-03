package com.schedek.curso.ejb;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class AppConfig {

    private static final String CONFIG_PREFIX = "config/";
    private Properties versionProperties = getVersionProperties();
    private Context initCtx;
    private Map<String, String> cache = new HashMap<String, String>();
    private static final Map<String, String> defaults = new HashMap<String, String>() {
        {
            put("root", "curso");
            put("dirPrefix", "curso-files");
        }
    };

    public AppConfig() {
        try {
            this.initCtx = new InitialContext();
        } catch (NamingException ex) {
            Logger.getLogger(AppConfig.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    String getProperty(String name) {
        if (name == null) {
            return null;
        }
        if (!cache.containsKey(name)) {
            cache.put(name, resolve(name));
        }
        return cache.get(name);
    }

    public String getRoot() {
        return getProperty("root");
    }

    public String getDirPrefix() {
        return getRoot() + File.separator + getProperty("dirPrefix");
    }

    public String getThumbRoot() {
        return getProperty("thumbRoot");
    }

    public String getContractUrl() {
        return getProperty("contract.url");
    }

    public String getInternalUrl() {
        return getProperty("internal.url");
    }

    public String getCustomerUrl() {
        return getProperty("customer.url");
    }

    public String getTaskmanagerUrl() {
        return getProperty("taskmanager.url");
    }

    public String getFioToken() {
        return getProperty("fio.token");
    }

    public String getFioAccountNr() {
        return getProperty("fio.account_nr");
    }

    public String getIntegrationJndiPath() {
        return getProperty("integration.jndi.path");
    }

    public String getIntegrationVersion() {
        return getProperty("integration.version");
    }

    public boolean isAirbnbSync() {
        return "1".equals(getProperty("airbnb.sync"));
    }

    public boolean isAirbnbVerbose() {
        return "1".equals(getProperty("airbnb.verbose"));
    }

    public String getRocketChatUsername() {
        return getProperty("rocketchat.username");
    }
    public String getRocketChatPassword() {
        return getProperty("rocketchat.password");
    }
    public String getSherlogUsername() {
        return getProperty("sherlog.username");
    }
    public String getSherlogPassword() {
        return getProperty("sherlog.password");
    }

    public String getBookingUsername() {
        return getProperty("booking.username");
    }

    public String getBookingPassword() {
        return getProperty("booking.password");
    }

    public String getBookingAuthorization() {
        return getProperty("booking.authorization");
    }

    private String resolve(String name) {
        if ("builddate".equals(name)
                || "version".equals(name)
                || "prefix".equals(name)
                || "contract.url".equals(name)
                || "customer.url".equals(name)
                || "taskmanager.url".equals(name)
                || "airbnb.sync".equals(name)
                || "airbnb.verbose".equals(name)
                || "integration.jndi.path".equals(name)
                || "integration.version".equals(name)
                || "internal.url".equals(name)) {
            return versionProperties.getProperty(name);
        }
        if ("root".equals(name)
                || "backupPrefix".equals(name)
                || "dirPrefix".equals(name)
                || "fio.account_nr".equals(name)
                || "fio.token".equals(name)
                || "rocketchat.username".equals(name)
                || "rocketchat.password".equals(name)
                || "sherlog.username".equals(name)
                || "sherlog.password".equals(name)
                || "booking.username".equals(name)
                || "booking.password".equals(name)
                || "booking.authorization".equals(name)
                || "thumbRoot".equals(name)
                || "flexibee.username".equals(name)
                || "flexibee.password".equals(name)
                || "flexibee.url".equals(name)
                || "flexibee.company".equals(name)) {
            String v = lookup(name);
            if (v == null) {
                v = defaults.get(name);
            }
            return v;
        }
        throw new UnsupportedOperationException("Unsupported configuration option");
    }

    public String getPrefix() {
        return getProperty("prefix");
    }

    public boolean isDev() {
        return "curso-dev".equals(getPrefix());
    }

    public String getFlexibeeUsername() {
        return getProperty("flexibee.username");
    }

    public String getFlexibeePassword() {
        return getProperty("flexibee.password");
    }

    public String getFlexibeeUrl() {
        return getProperty("flexibee.url");
    }

    public String getFlexibeeCompany() {
        return getProperty("flexibee.company");
    }

    private String lookup(String name) {
        try {
            return (String) initCtx.lookup(CONFIG_PREFIX + getProperty("prefix") + "/" + name);
        } catch (NamingException ex) {
//			Logger.getLogger(AppConfig.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private Properties getVersionProperties() {
        Properties r = new Properties();
        InputStream is = getClass().getResourceAsStream("version.properties");
        try {
            r.load(is);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(AppEJB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return r;
    }
}
