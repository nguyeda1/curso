package com.schedek.curso.ejb.view;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import com.schedek.curso.ejb.entities.Activity;

import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "v_activity")
@Cacheable(false)
public class ViewActivity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @OneToOne
    private Activity activity;
    private Long taskId;
    private Long caseId;
    private Long claimId;

    public Long getClaimId() {
        return claimId;
    }

    public void setClaimId(Long claimId) {
        this.claimId = claimId;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

}
