package actions;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Abstract class Action
 * 
 * @author HEXA-4112
 */
public abstract class Action {
    public abstract void executeAction (HttpServletRequest request) throws ServletException, IOException, ParseException;
}