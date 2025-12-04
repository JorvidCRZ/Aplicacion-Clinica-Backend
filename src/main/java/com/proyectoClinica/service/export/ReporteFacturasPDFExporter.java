package com.proyectoClinica.service.export;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.proyectoClinica.model.PagoDetalle;
import com.proyectoClinica.model.Persona;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Component
public class ReporteFacturasPDFExporter {

    private final DecimalFormat currencyFormat =
            new DecimalFormat("S/ #,##0.00", new DecimalFormatSymbols(new Locale("es", "PE")));


    // MÉTODOS

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

    private String getNombreCompleto(Persona persona) {
        return persona.getNombre1() + " "
                + (persona.getNombre2() != null ? persona.getNombre2() + " " : "")
                + persona.getApellidoPaterno() + " "
                + persona.getApellidoMaterno();
    }

    // MÉTODO EXPORTAR (COMPLETADO)

    public byte[] exportar(List<PagoDetalle> data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4, 36, 36, 54, 36);
            PdfWriter.getInstance(document, baos);
            document.open();

            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font fontSubtitulo = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font fontContenido = new Font(Font.FontFamily.HELVETICA, 9);
            Font bodyFont = new Font(Font.FontFamily.HELVETICA, 9);

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
                System.err.println("Advertencia: No se pudo cargar el logo del PDF de Facturas.");
            }

            PdfPCell logoCell = new PdfPCell();
            logoCell.setBorder(Rectangle.NO_BORDER);
            if (logo != null) logoCell.addElement(logo);
            encabezado.addCell(logoCell);

            PdfPCell textCell = new PdfPCell();
            textCell.setBorder(Rectangle.NO_BORDER);

            Paragraph titulo = new Paragraph("CLÍNICA TU SALUD", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            Paragraph subtituloReporte = new Paragraph("Reporte de Facturas", fontSubtitulo);
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

            // ======= 2. TABLA DE FACTURAS =======

            document.add(new Paragraph("Listado de Comprobantes de Pago", fontSubtitulo));
            document.add(Chunk.NEWLINE);

            // 4 Columnas: N° Comprobante, Detalle, Monto, Fecha Pago
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{2f, 5f, 2f, 2f});

            BaseColor headerColor = new BaseColor(0, 102, 204);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);

            // Headers
            agregarHeader(tabla, "N° Comprobante", headerFont, headerColor);
            agregarHeader(tabla, "Detalle del Servicio", headerFont, headerColor);
            agregarHeader(tabla, "Monto", headerFont, headerColor);
            agregarHeader(tabla, "Fecha de Pago", headerFont, headerColor);


            // Cuerpo de la Tabla
            for (PagoDetalle detalle : data) {

                String numComprobante = "F001-" + detalle.getPago().getIdPago();

                String servicio = detalle.getCita() != null ?
                        detalle.getCita().getMotivoConsulta() : "Servicio no especificado";

                String monto = currencyFormat.format(detalle.getMontoAsociado());

                LocalDateTime fechaPago = detalle.getPago().getFechaPago();
                String fechaStr = fechaPago != null ?
                        fechaPago.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A";


                agregarCelda(tabla, numComprobante, bodyFont);
                agregarCelda(tabla, servicio, bodyFont);
                agregarCelda(tabla, monto, bodyFont);
                agregarCelda(tabla, fechaStr, bodyFont);
            }

            if (data.isEmpty()) {
                PdfPCell noData = new PdfPCell(new Phrase("No hay registros de facturas disponibles", bodyFont));
                noData.setColspan(4);
                noData.setHorizontalAlignment(Element.ALIGN_CENTER);
                noData.setPadding(8);
                tabla.addCell(noData);
            }

            document.add(tabla);
            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF Reporte Facturas: " + e.getMessage(), e);
        }
    }
}