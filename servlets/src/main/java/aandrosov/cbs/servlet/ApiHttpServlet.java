package aandrosov.cbs.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import aandrosov.cbs.servlet.exception.*;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class ApiHttpServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            super.service(request, response);
        } catch (NotFoundException exception) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (NumberFormatException | BadRequestException exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}