package com.proyectoClinica.service.export;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.proyectoClinica.model.Medico;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

            // Encabezado con logo
            PdfPTable encabezadoTabla = new PdfPTable(2);
            encabezadoTabla.setWidthPercentage(100);
            encabezadoTabla.setWidths(new float[]{1f, 4f});

            Image logo = null;
            try (InputStream logoStream = getClass().getResourceAsStream("/static/images/logo.png")) {
                if (logoStream != null) {
                    logo = Image.getInstance(logoStream.readAllBytes());
                    logo.scaleToFit(90, 90); // tamaño profesional ajustado
                    logo.setAlignment(Element.ALIGN_LEFT);
                }
            }

            PdfPCell logoCell = new PdfPCell();
            if (logo != null) {
                logoCell.addElement(logo);
            }
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            encabezadoTabla.addCell(logoCell);

            // Encabezado
            PdfPCell textoEncabezado = new PdfPCell();
            textoEncabezado.setBorder(Rectangle.NO_BORDER);
            textoEncabezado.setVerticalAlignment(Element.ALIGN_MIDDLE);

            Paragraph titulo = new Paragraph("CLÍNICA TU SALUD", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);

            Paragraph subtitulo = new Paragraph("Reporte de Médicos", fontSubtitulo);
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

    // Celda del cuerpo
    private void agregarCelda(PdfPTable tabla, String texto, Font font) {
        PdfPCell celda = new PdfPCell(new Phrase(texto != null ? texto : "—", font));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPadding(5);
        tabla.addCell(celda);
    }
}
