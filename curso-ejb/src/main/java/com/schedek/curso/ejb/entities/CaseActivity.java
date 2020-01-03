/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Dan Nguyen
 */
@Entity
public class CaseActivity extends Activity {

    @ManyToOne
    private Case cursoCase;

    public CaseActivity() {
    }

    public Case getCursoCase() {
        return cursoCase;
    }

    public void setCursoCase(Case cursoCase) {
        this.cursoCase = cursoCase;
    }

}