/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.web.beans.common;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author Kubick
 */
public class AuthBean {
    
    protected long authItemId;
    
//    public Authentication getAuthentication() {
//        return authentication;
//    }

//    public long getAuthItemId() {
//        FrictionData fd = authentication.getAirlock().getUserFriction();
//        if(fd == null){
//            return 0;
//        }else{
//            return fd.getId();
//        }
//    }

    public void setAuthItemId(long authItemId) {
        if(authItemId != 0){
//            for(FrictionData fd : authentication.getAirlock().getFrictionData()){
//                if(authItemId == fd.getId()){
//                    authentication.getAirlock().setUserFriction(fd);
//                }
////            }
        }else{
//            authentication.getAirlock().setUserFriction(null);
        }
    }

        
    
}
