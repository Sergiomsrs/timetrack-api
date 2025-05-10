package org.tfg.timetrackapi.services;

public interface EmailService {

    public void enviarEmail(String destinatario, String asunto, String cuerpo);
}
