package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class ReadZipFileTest {

    @Test
    void zipFileTest() throws Exception {
        ZipFile zf = new ZipFile("src/test/resources/example.zip");

        ZipEntry csvEntry = zf.getEntry("example.csv");
        try (InputStream CsvStream = zf.getInputStream(csvEntry)) {
            CSVReader reader = new CSVReader(new InputStreamReader(CsvStream));
            List<String[]> list = reader.readAll();
            assertThat(list)
                    .hasSize(2)
                    .contains(
                            new String[]{"name", "  example_csv"},
                            new String[]{"type", "  csv"}
                    );
        }

        ZipEntry pdfEntry = zf.getEntry("example.pdf");
        try (InputStream pdfStream = zf.getInputStream(pdfEntry)) {
            PDF parsed = new PDF(pdfStream);
            assertThat(parsed.text).contains("type txt");
        }

        ZipEntry xlsxEntry = zf.getEntry("example.xlsx");
        try (InputStream xlsxStream = zf.getInputStream(xlsxEntry)) {
            XLS parsed = new XLS(xlsxStream);
            assertThat(parsed.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue())
                    .isEqualTo("xlsx");
        }
    }
}
