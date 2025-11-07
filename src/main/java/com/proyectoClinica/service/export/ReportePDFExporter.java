package com.proyectoClinica.service.export;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.proyectoClinica.model.Paciente;
import com.proyectoClinica.model.Persona;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ReportePDFExporter {

    public byte[] exportar(List<Paciente> pacientes) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
            PdfWriter.getInstance(document, baos);
            document.open();

            // 🔹 Fuentes
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font fontSubtitulo = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font fontContenido = new Font(Font.FontFamily.HELVETICA, 9);

            // 🔹 Fecha y hora actual
            LocalDateTime ahora = LocalDateTime.now();
            String fechaActual = ahora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String horaActual = ahora.format(DateTimeFormatter.ofPattern("HH:mm"));

            // 🔹 Encabezado principal
            Paragraph encabezado = new Paragraph("CLÍNICA TU SALUD", fontTitulo);
            encabezado.setAlignment(Element.ALIGN_CENTER);
            document.add(encabezado);

            Paragraph subtitulo = new Paragraph("Reporte de Pacientes", fontSubtitulo);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitulo);

            Paragraph fechaParrafo = new Paragraph("Fecha: " + fechaActual, fontContenido);
            fechaParrafo.setAlignment(Element.ALIGN_RIGHT);
            document.add(fechaParrafo);

            Paragraph horaParrafo = new Paragraph("Hora: " + horaActual, fontContenido);
            horaParrafo.setAlignment(Element.ALIGN_RIGHT);
            document.add(horaParrafo);

            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(12);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.2f, 4f, 4f, 2f, 3f, 3.5f, 1.9f, 3.2f, 3.8f, 5.5f, 5.5f, 2.5f});

            // Encabezado
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.WHITE);
            BaseColor headerColor = new BaseColor(0, 102, 204); // Azul elegante
            String[] headers = {
                    "ID", "Nombre", "Apellido", "Tipo Doc", "DNI", "Fecha Nac.",
                    "Edad", "Género", "Teléfono", "Correo", "Dirección", "Estado"
            };

            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setBackgroundColor(headerColor);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(6);
                table.addCell(cell);
            }

            // Cuerpo
            Font bodyFont = new Font(Font.FontFamily.HELVETICA, 9);
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (Paciente p : pacientes) {
                Persona persona = p.getPersona();
                LocalDate fechaNac = persona.getFechaNacimiento();
                int edad = (fechaNac != null) ? Period.between(fechaNac, LocalDate.now()).getYears() : 0;

                agregarCelda(table, String.valueOf(p.getIdPaciente()), bodyFont);
                agregarCelda(table, persona.getNombre1() + " " + (persona.getNombre2() != null ? persona.getNombre2() : ""), bodyFont);
                agregarCelda(table, persona.getApellidoPaterno() + " " + persona.getApellidoMaterno(), bodyFont);
                agregarCelda(table, persona.getTipoDocumento(), bodyFont);
                agregarCelda(table, persona.getDni(), bodyFont);
                agregarCelda(table, fechaNac != null ? fechaNac.format(formato) : "—", bodyFont);
                agregarCelda(table, String.valueOf(edad), bodyFont);
                agregarCelda(table, persona.getGenero(), bodyFont);
                agregarCelda(table, persona.getTelefono(), bodyFont);
                agregarCelda(table, p.getUsuarioAgrego() != null ? p.getUsuarioAgrego().getCorreo() : "—", bodyFont);
                agregarCelda(table, persona.getDireccion(), bodyFont);
                agregarCelda(table, "Activo", bodyFont);
            }

            document.add(table);
            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando el PDF de pacientes", e);
        }
    }

    // 🔹 Método reutilizable para celdas del cuerpo
    private void agregarCelda(PdfPTable tabla, String texto, Font font) {
        PdfPCell celda = new PdfPCell(new Phrase(texto != null ? texto : "—", font));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPadding(5);
        tabla.addCell(celda);
    }
}
