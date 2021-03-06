package richk.RMS.web.account;

import richk.RMS.Session;
import richk.RMS.database.DatabaseException;
import richk.RMS.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet({"/SignUp"})
public class SignUp extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int keyLength = 32;
    private String password;

    public SignUp() {
        super();
    }

    private Session getServerSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        Session session = (Session) httpSession.getAttribute("session");
        if (session == null) {
            try {
                session = new Session();
                httpSession.setAttribute("session", session);
            } catch (DatabaseException e) {
                httpSession.setAttribute("error", e);
                request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
            }
        }
        return session;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        Session session = getServerSession(request, response);

        try {
            // check if is not already logged
            if (session.getUser() == null) {

                String email = request.getParameter("email");
                String pass = request.getParameter("password");
                //    String name = request.getParameter("name");
                //    String lastname = request.getParameter("lastname");


                if (email != null) {
                    // se l'email è già presente nel DB
                    if (session.getDatabaseManager().isUserPresent(email)) {
                        // TODO password gia presente vuoi recuperarla? guarda se html o popup js
                    } else {
                        if (pass != null) {
                            if (pass.length() >= 8) {

                                session.getDatabaseManager().addUser(new User(email, pass, false));
                                // set userID into the session
                                session.setUser(email);

                                //httpSession.setAttribute("emailUser", email);
                                //response.sendRedirect("controlPanel.html");

                                //response.setHeader("Location", "/controlPanel.html");

                                response.sendRedirect("devices.html");
                                //request.getRequestDispatcher("controlPanel.html").forward(request, response);
                            } else {
                                // pass corta
                                // TODO rimanda da qualche parte perche c'è errore
                                httpSession.setAttribute("error", "pass corta");
                                request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
                            }
                        } else {
                            // mancano email o password
                            // TODO rimanda da qualche parte perche c'è errore
                            httpSession.setAttribute("error", "mancano email o password");
                            request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
                        }
                    }

                }
            }else{
                // already logged
                // TODO rimanda da qualche parte perche c'è errore
                httpSession.setAttribute("error", "già loggato");
                request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
            }


            //PrintWriter printWriter = response.getWriter();
            //printWriter.println("PTO");

//            String data = request.getParameter("email");
//            printWriter.println(data);
//            printWriter.flush();


        } catch (Exception e) {
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

}
