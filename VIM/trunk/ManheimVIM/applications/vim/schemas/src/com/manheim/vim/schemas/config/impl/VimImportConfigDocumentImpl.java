/*
 * An XML document type.
 * Localname: vim-import-config
 * Namespace: http://www.manheim.com/vim/schemas/config
 * Java type: com.manheim.vim.schemas.config.VimImportConfigDocument
 *
 * Automatically generated - do not modify.
 */
package com.manheim.vim.schemas.config.impl;
/**
 * A document containing one vim-import-config(@http://www.manheim.com/vim/schemas/config) element.
 *
 * This is a complex type.
 */
public class VimImportConfigDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements com.manheim.vim.schemas.config.VimImportConfigDocument
{
    private static final long serialVersionUID = 1L;
    
    public VimImportConfigDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName VIMIMPORTCONFIG$0 = 
        new javax.xml.namespace.QName("http://www.manheim.com/vim/schemas/config", "vim-import-config");
    
    
    /**
     * Gets the "vim-import-config" element
     */
    public com.manheim.vim.schemas.config.VimImportConfigDocument.VimImportConfig getVimImportConfig()
    {
        synchronized (monitor())
        {
            check_orphaned();
            com.manheim.vim.schemas.config.VimImportConfigDocument.VimImportConfig target = null;
            target = (com.manheim.vim.schemas.config.VimImportConfigDocument.VimImportConfig)get_store().find_element_user(VIMIMPORTCONFIG$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "vim-import-config" element
     */
    public void setVimImportConfig(com.manheim.vim.schemas.config.VimImportConfigDocument.VimImportConfig vimImportConfig)
    {
        synchronized (monitor())
        {
            check_orphaned();
            com.manheim.vim.schemas.config.VimImportConfigDocument.VimImportConfig target = null;
            target = (com.manheim.vim.schemas.config.VimImportConfigDocument.VimImportConfig)get_store().find_element_user(VIMIMPORTCONFIG$0, 0);
            if (target == null)
            {
                target = (com.manheim.vim.schemas.config.VimImportConfigDocument.VimImportConfig)get_store().add_element_user(VIMIMPORTCONFIG$0);
            }
            target.set(vimImportConfig);
        }
    }
    
    /**
     * Appends and returns a new empty "vim-import-config" element
     */
    public com.manheim.vim.schemas.config.VimImportConfigDocument.VimImportConfig addNewVimImportConfig()
    {
        synchronized (monitor())
        {
            check_orphaned();
            com.manheim.vim.schemas.config.VimImportConfigDocument.VimImportConfig target = null;
            target = (com.manheim.vim.schemas.config.VimImportConfigDocument.VimImportConfig)get_store().add_element_user(VIMIMPORTCONFIG$0);
            return target;
        }
    }
    /**
     * An XML vim-import-config(@http://www.manheim.com/vim/schemas/config).
     *
     * This is a complex type.
     */
    public static class VimImportConfigImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements com.manheim.vim.schemas.config.VimImportConfigDocument.VimImportConfig
    {
        private static final long serialVersionUID = 1L;
        
        public VimImportConfigImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName ID$0 = 
            new javax.xml.namespace.QName("http://www.manheim.com/vim/schemas/config", "id");
        private static final javax.xml.namespace.QName MAXEXECUTIONTIME$2 = 
            new javax.xml.namespace.QName("http://www.manheim.com/vim/schemas/config", "maxExecutionTime");
        private static final javax.xml.namespace.QName NUMBERTHREADS$4 = 
            new javax.xml.namespace.QName("http://www.manheim.com/vim/schemas/config", "numberThreads");
        
        
        /**
         * Gets the "id" element
         */
        public long getId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ID$0, 0);
                if (target == null)
                {
                    return 0L;
                }
                return target.getLongValue();
            }
        }
        
        /**
         * Gets (as xml) the "id" element
         */
        public org.apache.xmlbeans.XmlLong xgetId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlLong target = null;
                target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(ID$0, 0);
                return target;
            }
        }
        
        /**
         * Sets the "id" element
         */
        public void setId(long id)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ID$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ID$0);
                }
                target.setLongValue(id);
            }
        }
        
        /**
         * Sets (as xml) the "id" element
         */
        public void xsetId(org.apache.xmlbeans.XmlLong id)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlLong target = null;
                target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(ID$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlLong)get_store().add_element_user(ID$0);
                }
                target.set(id);
            }
        }
        
        /**
         * Gets the "maxExecutionTime" element
         */
        public long getMaxExecutionTime()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MAXEXECUTIONTIME$2, 0);
                if (target == null)
                {
                    return 0L;
                }
                return target.getLongValue();
            }
        }
        
        /**
         * Gets (as xml) the "maxExecutionTime" element
         */
        public org.apache.xmlbeans.XmlLong xgetMaxExecutionTime()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlLong target = null;
                target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(MAXEXECUTIONTIME$2, 0);
                return target;
            }
        }
        
        /**
         * Sets the "maxExecutionTime" element
         */
        public void setMaxExecutionTime(long maxExecutionTime)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MAXEXECUTIONTIME$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MAXEXECUTIONTIME$2);
                }
                target.setLongValue(maxExecutionTime);
            }
        }
        
        /**
         * Sets (as xml) the "maxExecutionTime" element
         */
        public void xsetMaxExecutionTime(org.apache.xmlbeans.XmlLong maxExecutionTime)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlLong target = null;
                target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(MAXEXECUTIONTIME$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlLong)get_store().add_element_user(MAXEXECUTIONTIME$2);
                }
                target.set(maxExecutionTime);
            }
        }
        
        /**
         * Gets the "numberThreads" element
         */
        public int getNumberThreads()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NUMBERTHREADS$4, 0);
                if (target == null)
                {
                    return 0;
                }
                return target.getIntValue();
            }
        }
        
        /**
         * Gets (as xml) the "numberThreads" element
         */
        public org.apache.xmlbeans.XmlInt xgetNumberThreads()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(NUMBERTHREADS$4, 0);
                return target;
            }
        }
        
        /**
         * Sets the "numberThreads" element
         */
        public void setNumberThreads(int numberThreads)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NUMBERTHREADS$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NUMBERTHREADS$4);
                }
                target.setIntValue(numberThreads);
            }
        }
        
        /**
         * Sets (as xml) the "numberThreads" element
         */
        public void xsetNumberThreads(org.apache.xmlbeans.XmlInt numberThreads)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(NUMBERTHREADS$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(NUMBERTHREADS$4);
                }
                target.set(numberThreads);
            }
        }
    }
}
