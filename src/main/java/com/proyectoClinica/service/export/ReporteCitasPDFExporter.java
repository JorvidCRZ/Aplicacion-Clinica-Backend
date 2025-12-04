package com.proyectoClinica.service.export;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.proyectoClinica.model.Cita;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReporteCitasPDFExporter {

    private void agregarHeader(PdfPTable tabla, String titulo, Font font, BaseColor bg) {
        PdfPCell cell = new PdfPCell(new Phrase(titulo, font));
        cell.setBackgroundColor(bg);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(6);
        tabla.addCell(cell);
    }

    private void agregarCelda(PdfPTable tabla, String texto, Font font) {
        PdfPCell celda = new PdfPCell(new Phrase(texto != null ? texto : "—", font));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPadding(5);
        tabla.addCell(celda);
    }

    private String getNombreCompleto(com.proyectoClinica.model.Persona persona) {
        return persona.getNombre1() + " "
                + (persona.getNombre2() != null ? persona.getNombre2() + " " : "")
                + persona.getApellidoPaterno() + " "
                + persona.getApellidoMaterno();
    }

    // MÉTODO EXPORTAR

    public byte[] exportar(List<Cita> data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
            PdfWriter.getInstance(document, baos);
            document.open();

            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font fontSubtitulo = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font fontContenido = new Font(Font.FontFamily.HELVETICA, 9);

            LocalDateTime ahora = LocalDateTime.now();
            String fechaActual = ahora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String horaActual = ahora.format(DateTimeFormatter.ofPattern("HH:mm"));

            // ENCABEZADO
            PdfPTable encabezado = new PdfPTable(2);
            encabezado.setWidthPercentage(100);
            encabezado.setWidths(new float[]{1f, 4f});

            Image logo = null;
            try (InputStream is = getClass().getResourceAsStream("/static/images/logo.png")) {
                if (is != null) {
                    logo = Image.getInstance(is.readAllBytes());
                    logo.scaleToFit(90, 90);
                }
            } catch (Exception e) {
                System.err.println("Advertencia: No se pudo cargar el logo del PDF. " + e.getMessage());
            }

            PdfPCell logoCell = new PdfPCell();
            logoCell.setBorder(Rectangle.NO_BORDER);
            if (logo != null) logoCell.addElement(logo);
            encabezado.addCell(logoCell);

            PdfPCell textCell = new PdfPCell();
            textCell.setBorder(Rectangle.NO_BORDER);

            Paragraph titulo = new Paragraph("CLÍNICA TU SALUD", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            Paragraph subtituloReporte = new Paragraph("Reporte de Citas", fontSubtitulo);
            subtituloReporte.setAlignment(Element.ALIGN_CENTER);

            Paragraph fechaP = new Paragraph("Fecha: " + fechaActual, fontContenido);
            fechaP.setAlignment(Element.ALIGN_RIGHT);
            Paragraph horaP = new Paragraph("Hora: " + horaActual, fontContenido);
            horaP.setAlignment(Element.ALIGN_RIGHT);

            textCell.addElement(titulo);
            textCell.addElement(subtituloReporte);
            textCell.addElement(fechaP);
            textCell.addElement(horaP);

            encabezado.addCell(textCell);
            document.add(encabezado);
            document.add(Chunk.NEWLINE);

            // TABLA DE CITAS

            document.add(new Paragraph("Listado de Citas Médicas", fontSubtitulo));
            document.add(Chunk.NEWLINE);

            PdfPTable tabla = new PdfPTable(8);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{1f, 4f, 4f, 3f, 2f, 1.5f, 2.5f, 2f});

            BaseColor headerColor = new BaseColor(0, 102, 204);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
            Font bodyFont = new Font(Font.FontFamily.HELVETICA, 9);

            // Headers
            agregarHeader(tabla, "ID", headerFont, headerColor);
            agregarHeader(tabla, "Paciente", headerFont, headerColor);
            agregarHeader(tabla, "Doctor", headerFont, headerColor);
            agregarHeader(tabla, "Especialidad", headerFont, headerColor);
            agregarHeader(tabla, "Fecha", headerFont, headerColor);
            agregarHeader(tabla, "Hora", headerFont, headerColor);
            agregarHeader(tabla, "Tipo Consulta", headerFont, headerColor);
            agregarHeader(tabla, "Estado", headerFont, headerColor);


            for (Cita cita : data) {
                String nombrePaciente = getNombreCompleto(cita.getPaciente().getPersona());
                String nombreDoctor = getNombreCompleto(cita.getDisponibilidad().getMedico().getPersona());

                String especialidadesStr = cita.getDisponibilidad().getMedico().getEspecialidades().stream()
                        .map(me -> me.getEspecialidad().getNombre())
                        .collect(Collectors.joining(", "));

                String motivoConsulta = cita.getMotivoConsulta() != null ?
                        cita.getMotivoConsulta() : "N/A";

                agregarCelda(tabla, String.valueOf(cita.getIdCita()), bodyFont);
                agregarCelda(tabla, nombrePaciente, bodyFont);
                agregarCelda(tabla, nombreDoctor, bodyFont);
                agregarCelda(tabla, especialidadesStr, bodyFont);
                agregarCelda(tabla, cita.getFechaCita().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), bodyFont);
                agregarCelda(tabla, cita.getHoraCita().format(DateTimeFormatter.ofPattern("HH:mm")), bodyFont);
                agregarCelda(tabla, motivoConsulta, bodyFont);
                agregarCelda(tabla, cita.getEstado(), bodyFont);
            }

            if (data.isEmpty()) {
                PdfPCell noData = new PdfPCell(new Phrase("No hay citas médicas registradas", bodyFont));
                noData.setColspan(8);
                noData.setHorizontalAlignment(Element.ALIGN_CENTER);
                noData.setPadding(8);
                tabla.addCell(noData);
            }

            document.add(tabla);
            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF Reporte Citas: " + e.getMessage(), e);
        }
    }
}