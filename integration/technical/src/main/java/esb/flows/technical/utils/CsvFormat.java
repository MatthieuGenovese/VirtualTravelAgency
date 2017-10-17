package esb.flows.technical.utils;

import org.apache.camel.model.dataformat.CsvDataFormat;

public class CsvFormat {

    public static CsvDataFormat buildCsvFormat() { // transforme le contenue du csv en Map<String Object> (les string correspondent a la premiere ligne du fichier)
        CsvDataFormat format = new CsvDataFormat();
        format.setDelimiter(",");
        format.setSkipHeaderRecord(true);
        format.setUseMaps(true);
        return format;
    }

}
