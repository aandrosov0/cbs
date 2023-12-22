package aandrosov.cbs.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import aandrosov.cbs.servlet.exception.NotFoundException;
import aandrosov.cbs.servlet.exception.BadRequestException;

import jakarta.servlet.annotation.WebServlet;

import jakarta.json.bind.JsonbBuilder;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import aandrosov.cbs.model.Book;

import aandrosov.cbs.database.repository.BookRepository;

@WebServlet("/book")
public class BookServlet extends HttpServlet {

    private final BookRepository repository = new BookRepository();

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long id = Long.parseLong(request.getParameter("id"));
        Book book = repository.read(id);

        if (book == null) {
            throw new NotFoundException();
        }

        response.addHeader("Content-Type", "application/json");
        JsonbBuilder.create().toJson(book, response.getWriter());
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String coverUrl = request.getParameter("cover_url");

        if (title == null || description == null || coverUrl == null) {
            throw new BadRequestException();
        }

        Book book = new Book(0, title, description, coverUrl);
        long id = repository.create(book);

        response.addHeader("Content-Type", "application/json");
        response.getWriter().println("{\"id\":" + id + "}");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long id = Long.parseLong(request.getParameter("id"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String coverUrl = request.getParameter("cover_url");

        if (title == null || description == null || coverUrl == null) {
            throw new BadRequestException();
        }

        if (!repository.update(new Book(id, title, description, coverUrl))) {
            throw new NotFoundException();
        }

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long id = Long.parseLong(request.getParameter("id"));

        if (!repository.delete(id)) {
            throw new NotFoundException();
        }

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}