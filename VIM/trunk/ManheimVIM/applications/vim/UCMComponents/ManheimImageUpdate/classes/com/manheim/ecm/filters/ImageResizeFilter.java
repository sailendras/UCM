/*     */ package com.manheim.ecm.filters;
/*     */ 
/*     */ import intradoc.common.ExecutionContext;
/*     */ import intradoc.common.FileUtils;
/*     */ import intradoc.common.Log;
/*     */ import intradoc.common.ServiceException;
/*     */ import intradoc.common.SystemUtils;
/*     */ import intradoc.data.DataBinder;
/*     */ import intradoc.data.DataException;
/*     */ import intradoc.data.Workspace;
/*     */ import intradoc.shared.FilterImplementor;
/*     */ import intradoc.shared.SharedObjects;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.InputStreamReader;
/*     */ 
/*     */ public class ImageResizeFilter
/*     */   implements FilterImplementor
/*     */ {
/*     */   public static final String CLASS_NAME = "ImageResizeFilter";
/*     */   public static final String TRACING_SECTION = "ImageResizing";
/*     */   public static final String SYS_PROP_OS_NAME = "os.name";
/*     */   public static final String WINDOWS_OS = "Windows";
/*     */   public static final String METADATA_NAME_DDOCTYPE = "dDocType";
/*     */   public static final String METADATA_NAME_DDOCNAME = "dDocName";
/*     */   public static final String METADATA_NAME_PRIM_FILE = "primaryFile";
/*     */   public static final String METADATA_NAME_PRIM_FILE_PATH = "primaryFile:path";
/*     */   public static final String METADATA_VALUE_DDOCTYPE_IMAGE = "Image";
/*     */   public static final String IMAGE_MAGICK_COMMAND_PATH = "ImageMagickCommandPath";
/*     */   public static final String IMG_MGK_RESIZE_OPT = "-resize";
/*     */   public static final String IMG_MGK_RESIZE_OPT_GEM_PARAMS = "800x600>";
/*     */   public static final String OPER_COMMA = ", ";
/*     */   public static final String DOUBLE_QUOTE = "";
/*     */   public static final String OPER_DOT = ".";
/*     */   public static final String CONVERSION_ERROR_MESSAGE = "Error in resizing image = ";
/*     */ 
/*     */   public int doFilter(Workspace ws, DataBinder binder, ExecutionContext cxt)
/*     */     throws DataException, ServiceException
/*     */   {
/*  57 */     String METHOD_NAME = ".doFilter() : ";
/*  58 */     trace(".doFilter() : ", "Entering in method");
/*     */ 
/*  61 */     String imageMagickCmd = SharedObjects.getEnvironmentValue("ImageMagickCommandPath");
/*     */ 
/*  63 */     String dDocType = binder.getLocal("dDocType");
/*  64 */     String dDocName = binder.getLocal("dDocName");
/*  65 */     String fileName = binder.getLocal("primaryFile");
/*  66 */     String format = null;
/*     */ 
/*  68 */     if (fileName != null)
/*     */     {
/*  70 */       int index = fileName.lastIndexOf(".");
/*     */ 
/*  72 */       if (index >= 0)
/*     */       {
/*  74 */         format = fileName.substring(index);
/*     */       }
/*     */     }
/*     */ 
/*  78 */     trace(".doFilter() : ", " dDocName = " + dDocName + " ; primaryFile = " + fileName + " format = " + format);
/*     */ 
/*  81 */     if ("Image".equalsIgnoreCase(dDocType))
/*     */     {
/*  83 */       String primaryFilePath = binder.getLocal("primaryFile:path");
/*     */ 
/*  85 */       if ((primaryFilePath != null) && (primaryFilePath.length() > 0))
/*     */       {
/*  87 */         long randFileName = DataBinder.getNextFileCounter();
/*  88 */         String tempDir = DataBinder.getTemporaryDirectory();
/*  89 */         String fullPath = tempDir + randFileName + format;
/*     */ 
/*  91 */         FileUtils.copyFile(primaryFilePath, fullPath);
/*     */ 
/*  93 */         trace(".doFilter() : ", " primaryFilePath = " + primaryFilePath + " ; fullPath =  " + fullPath);
/*     */ 
/*  95 */         BufferedReader errBufStream = null;
/*  96 */         int errorCode = -1;
/*  97 */         StringBuffer resizeErrMessage = new StringBuffer();
/*     */         try
/*     */         {
/* 101 */           ProcessBuilder procBuilder = new ProcessBuilder(
/* 102 */             new String[] { imageMagickCmd, "-resize", 
/* 103 */             "800x600>", fullPath, 
/* 104 */             primaryFilePath });
/*     */ 
/* 107 */           Process proc = procBuilder.start();
/*     */ 
/* 110 */           errBufStream = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
/*     */           String errorStreamLine;
/* 113 */           while ((errorStreamLine = errBufStream.readLine()) != null)
/*     */           {
/*     */             String errorStreamLine;
/* 115 */             resizeErrMessage.append(errorStreamLine);
/*     */           }
/*     */ 
/* 118 */           errorCode = proc.waitFor();
/* 119 */           trace(".doFilter() : ", " Error Code = " + errorCode + " ; Error Message =  " + resizeErrMessage);
/*     */         }
/*     */         catch (Exception e)
/*     */         {
/* 123 */           trace(".doFilter() : ", " EXCEPTION : Error Code = " + errorCode + " ; Error Message =  " + e.getMessage());
/* 124 */           Log.errorEx2("Error in resizing image = " + dDocName, "ImageResizing", e);
/*     */ 
/* 128 */           if (errBufStream != null)
/*     */             try
/*     */             {
/* 131 */               errBufStream.close();
/*     */             }
/*     */             catch (Exception e)
/*     */             {
/* 135 */               trace(".doFilter() : ", " EXCEPTION : Error Code = " + 
/* 136 */                 errorCode + " ; Error Message =  " + e.getMessage());
/* 137 */               Log.errorEx2("Error in resizing image = " + dDocName, "ImageResizing", e);
/*     */             }
/*     */         }
/*     */         finally
/*     */         {
/* 128 */           if (errBufStream != null) {
/*     */             try
/*     */             {
/* 131 */               errBufStream.close();
/*     */             }
/*     */             catch (Exception e)
/*     */             {
/* 135 */               trace(".doFilter() : ", " EXCEPTION : Error Code = " + 
/* 136 */                 errorCode + " ; Error Message =  " + e.getMessage());
/* 137 */               Log.errorEx2("Error in resizing image = " + dDocName, "ImageResizing", e);
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 142 */         if (errorCode != 0)
/*     */         {
/* 144 */           trace(".doFilter() : ", " EXCEPTION : Error Code = " + errorCode + 
/* 145 */             " ; Error Message =  " + resizeErrMessage.toString());
/*     */ 
/* 147 */           Log.errorEx("Error in resizing image = " + dDocName + " with Error Message :" + 
/* 148 */             resizeErrMessage.toString(), "ImageResizing");
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 153 */     trace(".doFilter() : ", "Exiting from method");
/* 154 */     return 0;
/*     */   }
/*     */ 
/*     */   private static void trace(String methodName, String traceMessage)
/*     */   {
/* 165 */     StringBuffer traceMsg = new StringBuffer();
/* 166 */     traceMsg.append("ImageResizeFilter");
/* 167 */     traceMsg.append(methodName);
/* 168 */     traceMsg.append(traceMessage);
/*     */ 
/* 170 */     SystemUtils.trace("ImageResizing", traceMsg.toString());
/*     */   }
/*     */ }

/* Location:           C:\dev\ucm\CustomComponents\
 * Qualified Name:     com.manheim.ecm.filters.ImageResizeFilter
 * JD-Core Version:    0.6.2
 */