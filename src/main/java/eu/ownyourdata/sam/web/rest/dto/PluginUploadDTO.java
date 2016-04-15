package eu.ownyourdata.sam.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

/**
 * Created by michael on 15.04.16.
 */
public class PluginUploadDTO {
    @NotNull
    @Lob
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private byte[] zip;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String zipContentType;

    public byte[] getZip() {
        return zip;
    }

    public void setZip(byte[] zip) {
        this.zip = zip;
    }

    public String getZipContentType() {
        return zipContentType;
    }

    public void setZipContentType(String zipContentType) {
        this.zipContentType = zipContentType;
    }
}
