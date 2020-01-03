package com.schedek.curso.ejb.view;

import com.schedek.curso.ejb.entities.User;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "v_user_stats")
@Cacheable(false)
public class UserStats implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @OneToOne
    private User user;
    @Column(nullable = false)
    private Long keys30d;
    private BigDecimal money;
    private Long avail30d;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getKeys30d() {
        return keys30d;
    }

    public void setKeys30d(Long keys30d) {
        this.keys30d = keys30d;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Long getAvail30d() {
        return avail30d;
    }

    public void setAvail30d(Long avail30d) {
        this.avail30d = avail30d;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.user);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserStats other = (UserStats) obj;
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserStats{" + "user=" + user + '}';
    }
}
