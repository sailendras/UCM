/*
 * An XML document type.
 * Localname: vim-import-config
 * Namespace: http://www.manheim.com/vim/schemas/config
 * Java type: com.manheim.vim.schemas.config.VimImportConfigDocument
 *
 * Automatically generated - do not modify.
 */
package com.manheim.vim.schemas.config;


/**
 * A document containing one vim-import-config(@http://www.manheim.com/vim/schemas/config) element.
 *
 * This is a complex type.
 */
public interface VimImportConfigDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(VimImportConfigDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s069CFC693AF607AF7F70B4AE5C971F51").resolveHandle("vimimportconfigf09cdoctype");
    
    /**
     * Gets the "vim-import-config" element
     */
    com.manheim.vim.schemas.config.VimImportConfigDocument.VimImportConfig getVimImportConfig();
    
    /**
     * Sets the "vim-import-config" element
     */
    void setVimImportConfig(com.manheim.vim.schemas.config.VimImportConfigDocument.VimImportConfig vimImportConfig);
    
    /**
     * Appends and returns a new empty "vim-import-config" element
     */
    com.manheim.vim.schemas.config.VimImportConfigDocument.VimImportConfig addNewVimImportConfig();
    
    /**
     * An XML vim-import-config(@http://www.manheim.com/vim/schemas/config).
     *
     * This is a complex type.
     */
    public interface VimImportConfig extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(VimImportConfig.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s069CFC693AF607AF7F70B4AE5C971F51").resolveHandle("vimimportconfig59bfelemtype");
        
        /**
         * Gets the "id" element
         */
        long getId();
        
        /**
         * Gets (as xml) the "id" element
         */
        org.apache.xmlbeans.XmlLong xgetId();
        
        /**
         * Sets the "id" element
         */
        void setId(long id);
        
        /**
         * Sets (as xml) the "id" element
         */
        void xsetId(org.apache.xmlbeans.XmlLong id);
        
        /**
         * Gets the "maxExecutionTime" element
         */
        long getMaxExecutionTime();
        
        /**
         * Gets (as xml) the "maxExecutionTime" element
         */
        org.apache.xmlbeans.XmlLong xgetMaxExecutionTime();
        
        /**
         * Sets the "maxExecutionTime" element
         */
        void setMaxExecutionTime(long maxExecutionTime);
        
        /**
         * Sets (as xml) the "maxExecutionTime" element
         */
        void xsetMaxExecutionTime(org.apache.xmlbeans.XmlLong maxExecutionTime);
        
        /**
         * Gets the "numberThreads" element
         */
        int getNumberThreads();
        
        /**
         * Gets (as xml) the "numberThreads" element
         */
        org.apache.xmlbeans.XmlInt xgetNumberThreads();
        
        /**
         * Sets the "numberThreads" element
         */
        void setNumberThreads(int numberThreads);
        
        /**
         * Sets (as xml) the "numberThreads" element
         */
        void xsetNumberThreads(org.apache.xmlbeans.XmlInt numberThreads);
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static com.manheim.vim.schemas.config.VimImportConfigDocument.VimImportConfig newInstance() {
              return (com.manheim.vim.schemas.config.VimImportConfigDocument.VimImportConfig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static com.manheim.vim.schemas.config.VimImportConfigDocument.VimImportConfig newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (com.manheim.vim.schemas.config.VimImportConfigDocument.VimImportConfig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static com.manheim.vim.schemas.config.VimImportConfigDocument newInstance() {
          return (com.manheim.vim.schemas.config.VimImportConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static com.manheim.vim.schemas.config.VimImportConfigDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (com.manheim.vim.schemas.config.VimImportConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static com.manheim.vim.schemas.config.VimImportConfigDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (com.manheim.vim.schemas.config.VimImportConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static com.manheim.vim.schemas.config.VimImportConfigDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (com.manheim.vim.schemas.config.VimImportConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static com.manheim.vim.schemas.config.VimImportConfigDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.manheim.vim.schemas.config.VimImportConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static com.manheim.vim.schemas.config.VimImportConfigDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.manheim.vim.schemas.config.VimImportConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static com.manheim.vim.schemas.config.VimImportConfigDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.manheim.vim.schemas.config.VimImportConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static com.manheim.vim.schemas.config.VimImportConfigDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.manheim.vim.schemas.config.VimImportConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static com.manheim.vim.schemas.config.VimImportConfigDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.manheim.vim.schemas.config.VimImportConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static com.manheim.vim.schemas.config.VimImportConfigDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.manheim.vim.schemas.config.VimImportConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static com.manheim.vim.schemas.config.VimImportConfigDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.manheim.vim.schemas.config.VimImportConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static com.manheim.vim.schemas.config.VimImportConfigDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.manheim.vim.schemas.config.VimImportConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static com.manheim.vim.schemas.config.VimImportConfigDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (com.manheim.vim.schemas.config.VimImportConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static com.manheim.vim.schemas.config.VimImportConfigDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (com.manheim.vim.schemas.config.VimImportConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static com.manheim.vim.schemas.config.VimImportConfigDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (com.manheim.vim.schemas.config.VimImportConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static com.manheim.vim.schemas.config.VimImportConfigDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (com.manheim.vim.schemas.config.VimImportConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static com.manheim.vim.schemas.config.VimImportConfigDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (com.manheim.vim.schemas.config.VimImportConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static com.manheim.vim.schemas.config.VimImportConfigDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (com.manheim.vim.schemas.config.VimImportConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
