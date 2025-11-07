package com.proyectoClinica.service.export;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.proyectoClinica.model.Paciente;
import com.proyectoClinica.model.Persona;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

            // Fuentes
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font fontSubtitulo = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font fontContenido = new Font(Font.FontFamily.HELVETICA, 9);

            // Fecha y hora
            LocalDateTime ahora = LocalDateTime.now();
            String fechaActual = ahora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String horaActual = ahora.format(DateTimeFormatter.ofPattern("HH:mm"));

            PdfPTable encabezadoTabla = new PdfPTable(2);
            encabezadoTabla.setWidthPercentage(100);
            encabezadoTabla.setWidths(new float[]{1f, 4f});

            Image logo = null;
            try (InputStream logoStream = getClass().getResourceAsStream("/static/images/logo.png")) {
                if (logoStream != null) {
                    logo = Image.getInstance(logoStream.readAllBytes());
                    logo.scaleToFit(90, 90);
                    logo.setAlignment(Element.ALIGN_LEFT);
                }
            }

            PdfPCell logoCell = new PdfPCell();
            if (logo != null) logoCell.addElement(logo);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            encabezadoTabla.addCell(logoCell);

            PdfPCell textoEncabezado = new PdfPCell();
            textoEncabezado.setBorder(Rectangle.NO_BORDER);
            textoEncabezado.setVerticalAlignment(Element.ALIGN_MIDDLE);

            Paragraph titulo = new Paragraph("CLÍNICA TU SALUD", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);

            Paragraph subtitulo = new Paragraph("Reporte de Pacientes", fontSubtitulo);
            subtitulo.setAlignment(Element.ALIGN_CENTER);

            Paragraph fechaParrafo = new Paragraph("Fecha: " + fechaActual, fontContenido);
            fechaParrafo.setAlignment(Element.ALIGN_RIGHT);

            Paragraph horaParrafo = new Paragraph("Hora: " + horaActual, fontContenido);
            horaParrafo.setAlignment(Element.ALIGN_RIGHT);

            textoEncabezado.addElement(titulo);
            textoEncabezado.addElement(subtitulo);
            textoEncabezado.addElement(fechaParrafo);
            textoEncabezado.addElement(horaParrafo);

            encabezadoTabla.addCell(textoEncabezado);
            document.add(encabezadoTabla);

            document.add(Chunk.NEWLINE);

            // Tabla de datos
            PdfPTable table = new PdfPTable(12);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.2f, 4f, 4f, 2f, 3f, 3.5f, 1.9f, 3.2f, 3.8f, 5.5f, 5.5f, 2.5f});

            // Encabezados
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
            BaseColor headerColor = new BaseColor(0, 102, 204);

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

    // Celdas del cuerpo
    private void agregarCelda(PdfPTable tabla, String texto, Font font) {
        PdfPCell celda = new PdfPCell(new Phrase(texto != null ? texto : "—", font));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPadding(5);
        tabla.addCell(celda);
    }
}
