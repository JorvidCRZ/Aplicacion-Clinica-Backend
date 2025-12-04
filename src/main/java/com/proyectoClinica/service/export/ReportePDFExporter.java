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

            // Fecha actual
            LocalDateTime ahora = LocalDateTime.now();
            String fechaActual = ahora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String horaActual = ahora.format(DateTimeFormatter.ofPattern("HH:mm"));

            // Encabezado
            PdfPTable encabezadoTabla = new PdfPTable(2);
            encabezadoTabla.setWidthPercentage(100);
            encabezadoTabla.setWidths(new float[]{1f, 4f});

            Image logo = null;
            try (InputStream logoStream = getClass().getResourceAsStream("/static/images/logo.png")) {
                if (logoStream != null) {
                    logo = Image.getInstance(logoStream.readAllBytes());
                    logo.scaleToFit(90, 90);
                }
            }

            PdfPCell logoCell = new PdfPCell();
            if (logo != null) logoCell.addElement(logo);
            logoCell.setBorder(Rectangle.NO_BORDER);
            encabezadoTabla.addCell(logoCell);
            PdfPCell textoEncabezado = new PdfPCell();
            textoEncabezado.setBorder(Rectangle.NO_BORDER);

            Paragraph titulo = new Paragraph("CLÍNICA TU SALUD", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            textoEncabezado.addElement(titulo);

            Paragraph subtitulo = new Paragraph("Reporte de Pacientes", fontSubtitulo);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            textoEncabezado.addElement(subtitulo);

            Paragraph fechaParrafo = new Paragraph("Fecha: " + fechaActual, fontContenido);
            fechaParrafo.setAlignment(Element.ALIGN_RIGHT);
            textoEncabezado.addElement(fechaParrafo);

            Paragraph horaParrafo = new Paragraph("Hora: " + horaActual, fontContenido);
            horaParrafo.setAlignment(Element.ALIGN_RIGHT);
            textoEncabezado.addElement(horaParrafo);

            encabezadoTabla.addCell(textoEncabezado);
            document.add(encabezadoTabla);

            document.add(Chunk.NEWLINE);

            // Tabla (8 columnas)
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.2f, 6f, 5f, 3f, 3f, 3f, 3.5f, 2.5f});

            // Encabezados
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
            BaseColor headerColor = new BaseColor(0, 102, 204);

            String[] headers = {
                    "ID", "Nombre Completo", "Correo", "Teléfono",
                    "Tipo Doc", "Número Doc", "Fecha Nac", "Género"
            };

            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setBackgroundColor(headerColor);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(6);
                table.addCell(cell);
            }

            // Contenido
            Font bodyFont = new Font(Font.FontFamily.HELVETICA, 9);
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (Paciente p : pacientes) {

                Persona persona = p.getPersona();
                LocalDate fechaNac = persona.getFechaNacimiento();

                String nombreCompleto =
                        persona.getNombre1() + " " +
                                (persona.getNombre2() != null ? persona.getNombre2() + " " : "") +
                                persona.getApellidoPaterno() + " " +
                                persona.getApellidoMaterno();

                agregarCelda(table, String.valueOf(p.getIdPaciente()), bodyFont);
                agregarCelda(table, nombreCompleto.trim(), bodyFont);
                agregarCelda(table, persona.getUsuario().getCorreo(), bodyFont);
                agregarCelda(table, persona.getTelefono(), bodyFont);
                agregarCelda(table, persona.getTipoDocumento(), bodyFont);
                agregarCelda(table, persona.getDni(), bodyFont);
                agregarCelda(table, fechaNac != null ? fechaNac.format(formatoFecha) : "—", bodyFont);
                agregarCelda(table, persona.getGenero(), bodyFont);
            }

            document.add(table);
            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF de pacientes", e);
        }
    }

    private void agregarCelda(PdfPTable tabla, String texto, Font font) {
        PdfPCell celda = new PdfPCell(new Phrase(texto != null ? texto : "—", font));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPadding(5);
        tabla.addCell(celda);
    }
}
