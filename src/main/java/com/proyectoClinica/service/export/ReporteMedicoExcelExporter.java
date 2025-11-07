package com.proyectoClinica.service.export;

import com.proyectoClinica.model.Medico;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReporteMedicoExcelExporter {

    public byte[] exportar(List<Medico> medicos) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Reporte de Médicos");

            sheet.setDisplayGridlines(false);

            Font fontTitulo = workbook.createFont();
            fontTitulo.setFontHeightInPoints((short) 16);
            fontTitulo.setBold(true);

            Font fontSubtitulo = workbook.createFont();
            fontSubtitulo.setFontHeightInPoints((short) 12);
            fontSubtitulo.setBold(true);

            CellStyle estiloTitulo = workbook.createCellStyle();
            estiloTitulo.setFont(fontTitulo);
            estiloTitulo.setAlignment(HorizontalAlignment.CENTER);

            CellStyle estiloSubtitulo = workbook.createCellStyle();
            estiloSubtitulo.setFont(fontSubtitulo);
            estiloSubtitulo.setAlignment(HorizontalAlignment.CENTER);

            CellStyle estiloEncabezado = workbook.createCellStyle();
            Font fontEncabezado = workbook.createFont();
            fontEncabezado.setBold(true);
            fontEncabezado.setColor(IndexedColors.WHITE.getIndex());
            estiloEncabezado.setFont(fontEncabezado);
            estiloEncabezado.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            estiloEncabezado.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            estiloEncabezado.setAlignment(HorizontalAlignment.CENTER);
            estiloEncabezado.setVerticalAlignment(VerticalAlignment.CENTER);
            estiloEncabezado.setBorderBottom(BorderStyle.THIN);
            estiloEncabezado.setBorderTop(BorderStyle.THIN);
            estiloEncabezado.setBorderLeft(BorderStyle.THIN);
            estiloEncabezado.setBorderRight(BorderStyle.THIN);

            CellStyle estiloCentrado = workbook.createCellStyle();
            estiloCentrado.setAlignment(HorizontalAlignment.CENTER);
            estiloCentrado.setVerticalAlignment(VerticalAlignment.CENTER);
            estiloCentrado.setWrapText(true);
            estiloCentrado.setBorderBottom(BorderStyle.THIN);
            estiloCentrado.setBorderTop(BorderStyle.THIN);
            estiloCentrado.setBorderLeft(BorderStyle.THIN);
            estiloCentrado.setBorderRight(BorderStyle.THIN);

            // Título principal
            Row filaTitulo = sheet.createRow(0);
            Cell celdaTitulo = filaTitulo.createCell(0);
            celdaTitulo.setCellValue("CLÍNICA TU SALUD");
            celdaTitulo.setCellStyle(estiloTitulo);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

            // Subtítulo
            Row filaSubtitulo = sheet.createRow(1);
            Cell celdaSubtitulo = filaSubtitulo.createCell(0);
            celdaSubtitulo.setCellValue("Reporte de Médicos");
            celdaSubtitulo.setCellStyle(estiloSubtitulo);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));

            // Fecha y hora
            LocalDateTime ahora = LocalDateTime.now();
            String fechaActual = ahora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String horaActual = ahora.format(DateTimeFormatter.ofPattern("HH:mm"));

            Row filaFecha = sheet.createRow(3);
            filaFecha.createCell(6).setCellValue("Fecha:");
            filaFecha.createCell(7).setCellValue(fechaActual);

            Row filaHora = sheet.createRow(4);
            filaHora.createCell(6).setCellValue("Hora:");
            filaHora.createCell(7).setCellValue(horaActual);

            // Encabezados
            String[] columnas = {"ID", "Nombre completo", "DNI", "Colegiatura", "Años Exp.", "Especialidades", "Teléfono", "Ubicación"};
            Row filaEncabezado = sheet.createRow(6);
            for (int i = 0; i < columnas.length; i++) {
                Cell celda = filaEncabezado.createCell(i);
                celda.setCellValue(columnas[i]);
                celda.setCellStyle(estiloEncabezado);
            }

            // Contenido
            int filaNum = 7;
            for (Medico medico : medicos) {
                Row fila = sheet.createRow(filaNum++);

                String nombreCompleto = medico.getPersona().getNombre1() +
                        (medico.getPersona().getNombre2() != null ? " " + medico.getPersona().getNombre2() : "") + " " +
                        medico.getPersona().getApellidoPaterno() + " " + medico.getPersona().getApellidoMaterno();

                String especialidades = medico.getEspecialidades() != null && !medico.getEspecialidades().isEmpty()
                        ? medico.getEspecialidades().stream()
                        .map(medicoEsp -> medicoEsp.getEspecialidad().getNombre())
                        .collect(Collectors.joining(", "))
                        : "Sin especialidad";

                Object[] datos = {
                        medico.getIdMedico(),
                        nombreCompleto,
                        medico.getPersona().getDni(),
                        medico.getColegiatura(),
                        medico.getExperienciaAnios() != null ? medico.getExperienciaAnios() : "—",
                        especialidades,
                        medico.getPersona().getTelefono() != null ? medico.getPersona().getTelefono() : "—",
                        medico.getPersona().getDistrito() != null ? medico.getPersona().getDistrito() : "—"
                };

                for (int i = 0; i < datos.length; i++) {
                    Cell celda = fila.createCell(i);
                    celda.setCellValue(String.valueOf(datos[i]));
                    celda.setCellStyle(estiloCentrado);
                }
            }

            // Ajustar columnas y limitar área visible
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Ocultar celdas vacías fuera del rango del reporte
            int ultimaFila = 7 + medicos.size();
            workbook.setPrintArea(0, 0, 7, 0, ultimaFila);

            sheet.setZoom(120);
            sheet.createFreezePane(0, 7);

            workbook.write(baos);
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte Excel de médicos: " + e.getMessage(), e);
        }
    }
}
