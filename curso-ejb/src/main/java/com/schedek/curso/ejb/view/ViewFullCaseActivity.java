package com.schedek.curso.ejb.view;

import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.enums.ActivityType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "v_full_case_activity")
@Cacheable(false)
public class ViewFullCaseActivity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "id")
    @Id
    private Long id;
    @JoinColumn(name = "cursocase_id")
    @ManyToOne
    private Case cursoCase;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdOn;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User createdBy;
    @Lob
    @Column
    private String log;
    @Enumerated(EnumType.STRING)
    private ActivityType type;

    public Long getId() {
        return id;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public Case getCursoCase() {
        return cursoCase;
    }

    public void setCursoCase(Case cursoCase) {
        this.cursoCase = cursoCase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewFullCaseActivity that = (ViewFullCaseActivity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ViewFullCaseActivity{" +
                "id=" + id +
                '}';
    }
}
