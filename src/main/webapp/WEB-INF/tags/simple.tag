<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ tag import="java.time.format.DateTimeFormatter"%>
<%@ tag import="java.time.LocalDate"%>
Current date: <%= DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now()) %>
