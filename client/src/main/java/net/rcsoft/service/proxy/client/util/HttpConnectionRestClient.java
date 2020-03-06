/*
 * Copyright (c) Critical Software S.A., All Rights Reserved.
 * (www.criticalsoftware.com)
 * This software is the proprietary information of Critical Software S.A.
 * Use is subject to license terms.
 */
package net.rcsoft.service.proxy.client.util;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Rest client implemented based on {@link HttpURLConnection}.
 *
 * @author recampelos
 */
public final class HttpConnectionRestClient {

    private static final String LINE_FEED = "\r\n";

    private static final int DEFAULT_CONNECTION_TIMEOUT = 5000;

    private static final int DEFAULT_READ_TIMEOUT = 10000;

    private final String boundary;
    private final HttpURLConnection httpConn;
    private final String charset;
    private final String method;
    private final String body;
    private final String contentType;
    private OutputStream outputStream;
    private PrintWriter writer;
    
    private HttpConnectionRestClient(
            final String requestURL, 
            final String method,
            final String body,
            final String contentType
    ) throws IOException {
        this.charset = "UTF-8";

        // creates a unique boundary based on time stamp
        this.boundary = "===" + System.currentTimeMillis() + "===";
        final URL url = new URL(requestURL);
        this.httpConn = (HttpURLConnection) url.openConnection();
        this.httpConn.setUseCaches(false);
        this.httpConn.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT);
        this.httpConn.setReadTimeout(DEFAULT_READ_TIMEOUT);
        this.method = method;
        this.body = body;
        this.contentType = contentType;
    }
    
    private void addFormField(final String name, final String value) {
        this.writer.append("--" + this.boundary).append(LINE_FEED);
        this.writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        this.writer.append("Content-Type: text/plain; charset=" + this.charset).append(
                LINE_FEED);
        this.writer.append(LINE_FEED);
        this.writer.append(value).append(LINE_FEED);
        this.writer.flush();
    }

    private void addFilePart(final String fieldName, final byte[] fileContent, final String fileName)
            throws IOException {
        this.writer.append("--" + this.boundary).append(LINE_FEED);
        this.writer.append(
                "Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        this.writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        this.writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        this.writer.append(LINE_FEED);
        this.writer.flush();

        this.outputStream.write(fileContent);
        this.outputStream.flush();
        this.writer.append(LINE_FEED);
        this.writer.flush();
    }

    private void setReadTimeOut(final int seconds) {
        this.httpConn.setReadTimeout(seconds * 1000);
    }

    private void setConnectionTimeOut(final int seconds) {
        this.httpConn.setConnectTimeout(seconds * 1000);
    }

    private void addHeaderField(final String name, final String value) {
        this.httpConn.setRequestProperty(name, value);
    }
    
    private void finaliseForm() {
        this.writer.append(LINE_FEED).flush();
        this.writer.append("--" + this.boundary + "--").append(LINE_FEED);
        this.writer.close();
    }
    
    private void writeBody() {
        this.writer.append(this.body);
        this.writer.flush();
        this.writer.close();
    }

    private HttpResponse execute() throws IOException {
        // checks server's status code first
        final StringBuilder response = new StringBuilder();
        final int status = this.httpConn.getResponseCode();
        final String message = this.httpConn.getResponseMessage();

        if (status >= 200 && status < 400) {
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(this.httpConn.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        } else if (status >= 400) {
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(this.httpConn.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        }
        
        return new HttpResponse(status, response.toString(), message);
    }
    
    private void connect() throws IOException {
        this.setContentType();
        this.httpConn.setRequestMethod(method);
        this.httpConn.setDoInput(true);

        if ("POST".equals(this.method) || "PUT".equals(this.method)) {
            this.httpConn.setDoOutput(true);
            Charset writerCharset = Charset.forName(this.charset);
            this.outputStream = this.httpConn.getOutputStream();
            this.writer = new PrintWriter(new OutputStreamWriter(this.outputStream, writerCharset), true);
        }
    }
    
    private void setContentType() {
        if (this.contentType != null && !this.contentType.trim().isEmpty()) {
            if ("multipart/form-data".equals(this.contentType)) {
                this.httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + this.boundary);
            } else {
                this.httpConn.setRequestProperty("Content-Type", this.contentType);
            }
        }
    }
    
    private void close() {
        if (this.httpConn != null) {
            this.httpConn.disconnect();
        }
    }
    
    /**
     * Get Request builder.
     * 
     * @param url endpoint URL
     * @return Builder instance
     */
    public static HttpConnectionRestClientBuilder getBuilder(final String url) {
        return new HttpConnectionRestClientBuilder(url);
    }
    
    /**
     * MultipartRequestBuilder.
     */
    public static final class HttpConnectionRestClientBuilder {
        
        private final String url;

        private int readTimeOut = -1;

        private int connectionTimeOut = -1;
        
        private final Map<String, String> headers = new HashMap<>();
        
        private final Map<String, String> formFields = new HashMap<>();
        
        private final Map<String, String> queryParameters = new HashMap<>();
        
        private final List<FilePart> files = new ArrayList<>();
        
        private final List<String> pathParts = new ArrayList<>();
        
        private String method;
        
        private Object body;
        
        private String contentType;

        private HttpConnectionRestClientBuilder(final String url) {
            this.url = url;
            this.method = "GET";
            this.contentType = "application/json";
        }
        
        /**
        * Adds a basic auth field to the request.
        *
        * @param userName  - name of the header field
        * @param password - value of the header field
        * @return builder instance
        */
       public HttpConnectionRestClientBuilder basicAuth(final String userName, final String password) {
           if (userName != null && password != null) {
               final String authorization = userName + ":" + password;
               final String encodedBytes =
                       Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8));

               this.headers.put("Authorization", "Basic " + encodedBytes);
           }

           return this;
       }
       
       /**
        * Adds a header field to the request.
        *
        * @param name  - name of the header field
        * @param value - value of the header field
        * @return builder instance
        */
       public HttpConnectionRestClientBuilder header(final String name, final String value) {
           this.headers.put(name, value);
           
           return this;
       }
       
       /**
        * Adds a form field to the request.
        *
        * @param name  - name of the form field
        * @param value - value of the form field
        * @return builder instance
        */
       public HttpConnectionRestClientBuilder field(final String name, final String value) {
           this.formFields.put(name, value);
           
           return this;
       }
       
       /**
        * Adds a query parameter to the request.
        *
        * @param name  - name of the form field
        * @param value - value of the form field
        * @return builder instance
        */
       public HttpConnectionRestClientBuilder queryParameter(final String name, final String value) {
           this.queryParameters.put(name, value);
           
           return this;
       }
       
       /**
        * Add file part to request.
        * 
        * @param fieldName field name
        * @param fileContent file content
        * @param fileName file name
        * @return builder instance
        */
       public HttpConnectionRestClientBuilder field(final String fieldName, final byte[] fileContent, final String fileName) {
           this.files.add(new FilePart(fieldName, fileContent, fileName));
           
           return this;
       }
       
       /**
         * Sets connection timeout.
         *
         * @param body num seconds for timeout
        * @param conentType body content type
         * @return builder instance
         */
        public HttpConnectionRestClientBuilder body(final Object body, final String conentType) {
            this.body = body;
            this.contentType = conentType;

            return this;
        }

        /**
         * Sets connection timeout.
         *
         * @param seconds num seconds for timeout
         * @return builder instance
         */
        public HttpConnectionRestClientBuilder connectionTimeOut(final int seconds) {
            this.connectionTimeOut = seconds;

            return this;
        }

        /**
         * Sets read timeout.
         *
         * @param seconds num seconds for timeout
         * @return builder instance
         */
        public HttpConnectionRestClientBuilder readTimeOut(final int seconds) {
            this.readTimeOut = seconds;

            return this;
        }
        
        /**
         * Sets read timeout.
         *
         * @param path yrl path
         * @return builder instance
         */
        public HttpConnectionRestClientBuilder path(final String path) {
            if (path != null && !path.trim().isEmpty()) {
                if (path.startsWith("/")) {
                    this.pathParts.add(path.substring(1));
                } else {
                    this.pathParts.add(path);
                }
            }

            return this;
        }
        
        /**
         * Executes GET Http request.
         * 
         * @return {@see HttpResponse} object with response data
         * @throws IOException in case of error
         */
        public HttpResponse get() throws IOException {
            this.method = "GET";
            
            return this.send();
        }
        
        /**
         * Executes POST Http request.
         * 
         * @return {@see HttpResponse} object with response data
         * @throws IOException in case of error
         */
        public HttpResponse post() throws IOException {
            this.method = "POST";
            
            return this.send();
        }
        
        /**
         * Executes PUT Http request.
         * 
         * @return {@see HttpResponse} object with response data
         * @throws IOException in case of error
         */
        public HttpResponse put() throws IOException {
            this.method = "PUT";
            
            return this.send();
        }
        
        /**
         * Executes DELETE Http request.
         * 
         * @return {@see HttpResponse} object with response data
         * @throws IOException in case of error
         */
        public HttpResponse delete() throws IOException {
            this.method = "DELETE";
            
            return this.send();
        }

        /**
         *  Send request.
         *
         * @return {@see HttpResponse} object with response data
         * @throws IOException in case of error
         */
       private HttpResponse send() throws IOException {
           HttpResponse result = null;
           HttpConnectionRestClient request = null;
           
           try {
               String finalBody = "";
               
               // Verify content type in case of files or fields existing
               if (!this.formFields.isEmpty() || !this.files.isEmpty()) {
                   this.contentType = "multipart/form-data";
                   this.body = null;
                   
                   if (!"POST".equals(this.method) || !"PUT".equals(this.method)) {
                       this.method = "POST";
                   }
               } else if (this.body != null) {
                   if ("application/json".equals(this.contentType)) {
                       finalBody = new Gson().toJson(this.body);
                   } else {
                       finalBody = this.body.toString();
                   }
               }
               
               String finalUrl = this.buildFinalUrl();
               
               request = new HttpConnectionRestClient(finalUrl, this.method, finalBody, this.contentType);

               if (this.readTimeOut > -1) {
                   request.setReadTimeOut(this.readTimeOut);
               }

               if (this.connectionTimeOut > -1) {
                   request.setConnectionTimeOut(this.connectionTimeOut);
               }

               if (!this.headers.isEmpty()) {
                   for (final Map.Entry<String, String> header : this.headers.entrySet()) {
                       request.addHeaderField(header.getKey(), header.getValue());
                   }
               }
               
               request.connect();
               
               if ("POST".equals(this.method) || "PUT".equals(this.method)) {
                    if ("multipart/form-data".equals(this.contentType)) {
                         if (!this.formFields.isEmpty()) {
                             for (final Map.Entry<String, String> field : this.formFields.entrySet()) {
                                 request.addFormField(field.getKey(), field.getValue());
                             }
                         }

                         if (!this.files.isEmpty()) {
                             for (final FilePart file : this.files) {
                                 request.addFilePart(file.getFieldName(), file.getFileContent(), file.getFileName());
                             }
                         }

                         request.finaliseForm();
                    } else {
                        request.writeBody();
                    }
               }
               
               result = request.execute();
            } finally {
               if (request != null) {
                   request.close();
               }
           }
           
           return result;
       }
       
       private String buildFinalUrl() {
            StringBuilder finalUrl = new StringBuilder();
            StringBuilder queryString = new StringBuilder("?");
           
            if (this.url.indexOf('?') != -1) {
                final String[] urlParts = this.url.split("\\?");

                finalUrl.append(urlParts[0]);

                if (urlParts.length > 1) {
                    queryString.append(urlParts[1]);
                }
            }
           
            if (!this.queryParameters.isEmpty()) {
                this.queryParameters.entrySet().stream().map((param) -> {
                    if (queryString.length() > 1) {
                        queryString.append('&');
                    }
                    queryString.append(param.getKey());
                    return param;
                }).forEachOrdered((param) -> {
                    queryString.append('=');
                    queryString.append(param.getValue());
                });
            } else {
                finalUrl.append(this.url);
            }
            
            if (!this.pathParts.isEmpty()) {
                this.pathParts.forEach((path) -> {
                    if (!finalUrl.toString().endsWith("/")) {
                        finalUrl.append('/');
                    }
                    
                    finalUrl.append(path);
                });
            }
            
            if (queryString.toString().length() > 1) {
                finalUrl.append(queryString);
            }

            return finalUrl.toString();
       }
    }

    /**
     * Class for upload file parts.
     */
    private static final class FilePart {
        private final String fieldName;
        private final byte[] fileContent;
        private final String fileName;

        private FilePart(final String fieldName, final byte[] fileContent, final String fileName) {
            this.fieldName = fieldName;
            this.fileContent = fileContent;
            this.fileName = fileName;
        }

        public String getFieldName() {
            return this.fieldName;
        }

        public byte[] getFileContent() {
            return this.fileContent;
        }

        public String getFileName() {
            return this.fileName;
        }
    }

    /**
     * Class representing a http response.
     */
    public static final class HttpResponse {
        
        private final int status;

        private final String body;

        private final String statusMessage;

        private HttpResponse(final int status, final String body, final String statusMessage) {
            this.status = status;
            this.body = body;
            this.statusMessage = statusMessage;
        }

        /**
         * Get response status.
         *
         * @return status
         */
        public int getStatus() {
            return this.status;
        }

        /**
         * Get response body.
         *
         * @return status
         */
        public String getBody() {
            return this.body;
        }

        /**
         * Get response status message.
         *
         * @return status
         */
        public String getStatusMessage() {
            return this.statusMessage;
        }

        /**
         * Get response body as type.
         *
         * @param type type to return
         * @param <T> type
         * @return intance of type
         */
        public <T> T getBody(final Class<T> type) {
            return new Gson().fromJson(this.body, type);
        }

        /**
         * Get if request was successful.
         *
         * @return true if request was successful
         */
        public boolean isSuccess() {
            return this.status < 400;
        }
    }
}
