package com.schedek.curso.ejb.entities;

import com.schedek.curso.ejb.util.Security;
import com.schedek.curso.ejb.view.UserStats;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Root
 */
@Entity
@Table(name = "sysuser")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String phone;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp DEFAULT NOW()")
    private Date created = new Date();
    @ManyToMany
    private List<Group> groups = new ArrayList<>();
    @ManyToOne
    private User createdBy;
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private UserStats stats;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastActivityFetch = new Date();
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastActivityRead = new Date();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return "";
    }

    public boolean isPasswordValid(String pass) {
        if (password != null && password.equals(Security.md5(pass))) {
            return true;
        }
        return false;
    }

    public boolean isPasswordDigestValid(String pass) {
        if (password != null && password.equals(pass)) {
            return true;
        }
        return false;
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return;
        }
        this.password = Security.md5(password);
    }
    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreated() {
        return created;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlTransient
    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public String getFullname() {
        String toReturn = getFirstname() + " " + getLastname();
        if (" ".equals(toReturn)) {
            return username;
        }
        return toReturn;
    }

    @XmlTransient
    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isRole(String name) {
        List<Group> groups = getGroups();
        for (Group g : groups) {
            if (g.getName() != null && g.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAdmin() {
        return isRole("admin");
    }

    public boolean isSales() {
        return isRole("sales");
    }

    public boolean isInternalSales() {
        return isRole("internalsales");
    }

    public boolean isOperationsManagement() {
        return isRole("operationsmanagement");
    }

    public boolean isVehiclesManager() {
        return isRole("vehicles_manager");
    }

    public boolean isOperator() {
        return isRole("operator");
    }

    public boolean isHrSupport() {
        return isRole("hr_support");
    }

    public boolean isKeymaster() {
        return isRole("keymaster");
    }

    public boolean isSupplyCheck() {
        return isRole("supply_check");
    }

    public boolean isListingsManager() {
        return isRole("listingsmanager");
    }

    public boolean isWarehouse() {
        return isRole("warehouse");
    }

    public boolean isCleaning() {
        return isRole("cleaning");
    }

    public boolean isHandyman() {
        return isRole("handyman");
    }

    public boolean isAccountantExternal() {
        return isRole("accountant_external");
    }

    public boolean isListingCheck() {
        return isRole("listing_check");
    }

    public boolean isReception() {
        return isRole("reception");
    }

    public boolean isAccountantInternal() {
        return isRole("accountant_internal");
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserStats getStats() {
        return stats;
    }



    public boolean isDisabled() {
        if (groups == null) {
            return false;
        }
        return groups.isEmpty();
    }


    public Date getLastActivityFetch() {
        if (lastActivityFetch == null) {
            lastActivityFetch = new Date();
        }
        return lastActivityFetch;
    }

    public void setLastActivityFetch(Date lastActivityFetch) {
        this.lastActivityFetch = lastActivityFetch;
    }

    public Date getLastActivityRead() {
        if (lastActivityRead == null) {
            lastActivityRead = new Date();
        }
        return lastActivityRead;
    }

    public void setLastActivityRead(Date lastActivityRead) {
        this.lastActivityRead = lastActivityRead;
    }

      
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.schedek.curso.ejb.entities.User[ id=" + id + " ]";
    }

}
