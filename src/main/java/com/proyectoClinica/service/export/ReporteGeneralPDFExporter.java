package com.proyectoClinica.service.export;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.proyectoClinica.dto.response.EstadoCitaDTO;
import com.proyectoClinica.dto.response.ReporteGeneralDTO;
import com.proyectoClinica.dto.response.TopDoctorDTO;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ReporteGeneralPDFExporter {

    public byte[] exportar(ReporteGeneralDTO data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4, 36, 36, 54, 36);
            PdfWriter.getInstance(document, baos);
            document.open();

            // FUENTES
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

            // Logo
            Image logo = null;
            try (InputStream is = getClass().getResourceAsStream("/static/images/logo.png")) {
                if (is != null) {
                    logo = Image.getInstance(is.readAllBytes());
                    logo.scaleToFit(90, 90);
                }
            }
            PdfPCell logoCell = new PdfPCell();
            logoCell.setBorder(Rectangle.NO_BORDER);
            if (logo != null) logoCell.addElement(logo);
            encabezado.addCell(logoCell);

            // Textos encabezado
            PdfPCell textCell = new PdfPCell();
            textCell.setBorder(Rectangle.NO_BORDER);

            Paragraph titulo = new Paragraph("CLÍNICA TU SALUD", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);

            Paragraph subtitulo = new Paragraph("Reporte General", fontSubtitulo);
            subtitulo.setAlignment(Element.ALIGN_CENTER);

            Paragraph fechaP = new Paragraph("Fecha: " + fechaActual, fontContenido);
            fechaP.setAlignment(Element.ALIGN_RIGHT);

            Paragraph horaP = new Paragraph("Hora: " + horaActual, fontContenido);
            horaP.setAlignment(Element.ALIGN_RIGHT);

            textCell.addElement(titulo);
            textCell.addElement(subtitulo);
            textCell.addElement(fechaP);
            textCell.addElement(horaP);

            encabezado.addCell(textCell);
            document.add(encabezado);
            document.add(Chunk.NEWLINE);

            // RESUMEN GENERAL DEL ESTADO DE CITAS
            PdfPTable tablaEstados = new PdfPTable(3);
            tablaEstados.setWidthPercentage(100);
            tablaEstados.setWidths(new float[]{3f, 2f, 2f});

            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
            BaseColor headerColor = new BaseColor(0, 102, 204);
            Font bodyFont = new Font(Font.FontFamily.HELVETICA, 9);

            // ENCABEZADOS
            agregarHeader(tablaEstados, "Estado", headerFont, headerColor);
            agregarHeader(tablaEstados, "Cantidad", headerFont, headerColor);
            agregarHeader(tablaEstados, "Porcentaje", headerFont, headerColor);

            EstadoCitaDTO est = data.getEstadoCitas();

            agregarCelda(tablaEstados, "Confirmadas", bodyFont);
            agregarCelda(tablaEstados, String.valueOf(est.getConfirmadas()), bodyFont);
            agregarCelda(tablaEstados, est.getPorcentajeConfirmadas() + "%", bodyFont);

            agregarCelda(tablaEstados, "Pendientes", bodyFont);
            agregarCelda(tablaEstados, String.valueOf(est.getPendientes()), bodyFont);
            agregarCelda(tablaEstados, est.getPorcentajePendientes() + "%", bodyFont);

            agregarCelda(tablaEstados, "Canceladas", bodyFont);
            agregarCelda(tablaEstados, String.valueOf(est.getCanceladas()), bodyFont);
            agregarCelda(tablaEstados, est.getPorcentajeCanceladas() + "%", bodyFont);

            document.add(new Paragraph("Resumen General de Citas", fontSubtitulo));
            document.add(Chunk.NEWLINE);
            document.add(tablaEstados);
            document.add(Chunk.NEWLINE);


            // TOP DOCTORES

            document.add(new Paragraph("Top Doctores", fontSubtitulo));
            document.add(Chunk.NEWLINE);

            PdfPTable tablaTop = new PdfPTable(3);
            tablaTop.setWidthPercentage(100);
            tablaTop.setWidths(new float[]{4f, 3f, 2f});

            agregarHeader(tablaTop, "Doctor", headerFont, headerColor);
            agregarHeader(tablaTop, "Especialidad", headerFont, headerColor);
            agregarHeader(tablaTop, "Citas", headerFont, headerColor);

            List<TopDoctorDTO> topDocs = data.getTopDoctores();

            for (TopDoctorDTO d : topDocs) {
                agregarCelda(tablaTop, d.getNombreCompleto(), bodyFont);
                agregarCelda(tablaTop, d.getEspecialidad(), bodyFont);
                agregarCelda(tablaTop, String.valueOf(d.getTotalCitas()), bodyFont);
            }

            if (topDocs.isEmpty()) {
                PdfPCell noData = new PdfPCell(new Phrase("No hay doctores con citas registradas.", bodyFont));
                noData.setColspan(3);
                noData.setHorizontalAlignment(Element.ALIGN_CENTER);
                noData.setPadding(8);
                tablaTop.addCell(noData);
            }

            document.add(tablaTop);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF general: " + e.getMessage(), e);
        }
    }

    // UTILIDADES DE TABLA

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
}
