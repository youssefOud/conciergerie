package Servlet;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public abstract class Action {
	public abstract void executeAction (HttpServletRequest request) throws ServletException, IOException, ParseException;
}
