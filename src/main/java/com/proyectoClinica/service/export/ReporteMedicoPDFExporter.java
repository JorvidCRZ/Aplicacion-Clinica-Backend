package com.proyectoClinica.service.export;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.proyectoClinica.model.Medico;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReporteMedicoPDFExporter {

    public byte[] exportar(List<Medico> medicos) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
            PdfWriter.getInstance(document, baos);
            document.open();

            // Fuentes
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font fontSubtitulo = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font fontContenido = new Font(Font.FontFamily.HELVETICA, 9);

            // Fecha y hora actual
            LocalDateTime ahora = LocalDateTime.now();
            String fechaActual = ahora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String horaActual = ahora.format(DateTimeFormatter.ofPattern("HH:mm"));

            // Encabezado principal
            Paragraph encabezado = new Paragraph("CLÍNICA TU SALUD", fontTitulo);
            encabezado.setAlignment(Element.ALIGN_CENTER);
            document.add(encabezado);

            Paragraph subtitulo = new Paragraph("Reporte de Médicos", fontSubtitulo);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitulo);

            Paragraph fechaParrafo = new Paragraph("Fecha: " + fechaActual, fontContenido);
            fechaParrafo.setAlignment(Element.ALIGN_RIGHT);
            document.add(fechaParrafo);

            Paragraph horaParrafo = new Paragraph("Hora: " + horaActual, fontContenido);
            horaParrafo.setAlignment(Element.ALIGN_RIGHT);
            document.add(horaParrafo);

            document.add(Chunk.NEWLINE);

            PdfPTable tabla = new PdfPTable(8);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{1, 3, 2, 2, 2, 3, 2, 2});

            // Encabezados
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
            BaseColor headerColor = new BaseColor(0, 102, 204); // Azul elegante
            String[] headers = {
                    "ID", "Nombre completo", "DNI", "Colegiatura",
                    "Años Exp.", "Especialidades", "Teléfono", "Ubicación"
            };

            for (String h : headers) {
                PdfPCell celda = new PdfPCell(new Phrase(h, headerFont));
                celda.setBackgroundColor(headerColor);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setPadding(6);
                tabla.addCell(celda);
            }

            // Cuerpo
            Font bodyFont = new Font(Font.FontFamily.HELVETICA, 9);
            for (Medico medico : medicos) {
                String nombreCompleto = medico.getPersona().getNombre1() +
                        (medico.getPersona().getNombre2() != null ? " " + medico.getPersona().getNombre2() : "") + " " +
                        medico.getPersona().getApellidoPaterno() + " " + medico.getPersona().getApellidoMaterno();

                String especialidades = (medico.getEspecialidades() != null && !medico.getEspecialidades().isEmpty())
                        ? medico.getEspecialidades().stream()
                        .map(medicoEsp -> medicoEsp.getEspecialidad().getNombre())
                        .collect(Collectors.joining(", "))
                        : "Sin especialidad";

                agregarCelda(tabla, String.valueOf(medico.getIdMedico()), bodyFont);
                agregarCelda(tabla, nombreCompleto, bodyFont);
                agregarCelda(tabla, medico.getPersona().getDni(), bodyFont);
                agregarCelda(tabla, medico.getColegiatura(), bodyFont);
                agregarCelda(tabla, medico.getExperienciaAnios() != null ? String.valueOf(medico.getExperienciaAnios()) : "—", bodyFont);
                agregarCelda(tabla, especialidades, bodyFont);
                agregarCelda(tabla, medico.getPersona().getTelefono() != null ? medico.getPersona().getTelefono() : "—", bodyFont);
                agregarCelda(tabla, medico.getPersona().getDistrito() != null ? medico.getPersona().getDistrito() : "—", bodyFont);
            }

            document.add(tabla);
            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte PDF de médicos: " + e.getMessage(), e);
        }
    }

    // Celda de encabezado
    private void agregarCeldaEncabezado(PdfPTable tabla, String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE)));
        celda.setBackgroundColor(new BaseColor(0, 102, 204)); // Azul profesional
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(6);
        tabla.addCell(celda);
    }

    // Celda del cuerpo
    private void agregarCelda(PdfPTable tabla, String texto, Font font) {
        PdfPCell celda = new PdfPCell(new Phrase(texto != null ? texto : "—", font));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPadding(5);
        tabla.addCell(celda);
    }
}
