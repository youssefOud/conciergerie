/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

import Services.Services;
import java.io.IOException;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author X
 */
public class ActionReportAd extends Action {

    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        // TODO : envoyer mail au moderateur (moderateur@gmail.com)
        String idAd = request.getParameter("idAnnonce");
        Long idAdLong = Long.valueOf(idAd);
                
        Services services = new Services();
        boolean reported = services.reportAd(idAdLong);
        
        request.setAttribute("reported", reported);
    }
    
}
