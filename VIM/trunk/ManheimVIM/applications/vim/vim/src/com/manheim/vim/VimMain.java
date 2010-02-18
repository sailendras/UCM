package com.manheim.vim;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manheim.vim.imageexporter.ImageExporter;
import com.manheim.vim.imageloader.ImageLoader;
import com.manheim.vim.schemas.config.export.ExportConfigDocument.ExportConfig;
import com.manheim.vim.schemas.config.manifest.ManifestDocument.Manifest;
import com.manheim.vim.schemas.config.vim.VimConfigDocument.VimConfig;
import com.manheim.vim.schemas.config.ximport.ImportConfigDocument.ImportConfig;
import com.manheim.vim.services.util.ExceptionHelper;
import com.manheim.vim.services.util.XmlBeanHelper;

/**
 * VimMain.java - Entry point to the VIM application. It is responsible for parsing arguments and 
 * initializing Import or Export sequence.
 * @author Enterpulse
 * @version 1.0 
 * @see VimCommand
 */
public class VimMain
{
	/** Helpful instructions regarding correct usage */
    private final String USAGE = "java -jar vim.jar [-h]\n" + 
                                 "[-e <export config file> -c <vim config file>]\n" + 
                                 "[-i <import config file> -c <vim config file> -m <manifest file>]";
    private final String HEADER = "\n\nOptions:";

    /** Sample execution commands prompting correct usage */
    private final String FOOTER = "\n\nExamples:\n\n" +
            "Export:  java -jar vim.jar -e export-config.xml -c vim-config.xml\n\n\n" +
            "Import:  java -jar vim.jar -i import-config.xml -c vim-config.xml -m manifest.xml";
    
    /** Logger instance for VimMain class */
    private static final Logger log = LoggerFactory.getLogger(VimMain.class);
    
    /**
     * The main method of VimMain class.
     * @param args An array of Strings as arguments
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException
    {
    	VimMain vimMain = new VimMain();
    	vimMain.execute(args);
    }
    
    /**
     * This method is generates command line options, 
     * instantiates either ImageLoader or ImageExporter objects and 
     * call the execute method on them. This initiates either import 
     * or export sequence based on the command line arguments
     * @param args An array of Strings as arguments
     */
    private void execute(String[] args)
    {
        Options options = generateCommandLineOptions();
        
        try
        {
            log.debug("Executing VIM - " + args);
            
            long startTime = System.currentTimeMillis();
            
            VimCommand command = parseCommandLine(args);
            
            command.execute();
            
            long endTime = System.currentTimeMillis();
            
            String message = "COMPLETED - Total processing time = " + (endTime - startTime);
    
            System.out.println(message);
            log.debug(message);
        }
        catch (MissingOptionException e)
        {
            e.printStackTrace();
            printCommandLineUsage(options);
        }
        catch (Exception e)
        {
            log.error(ExceptionHelper.getStackTraceAsString(e));
            e.printStackTrace();
        }
    }
    
    /**
     * Generates command line options, parses the command line arguments, 
     * validates them against the options and instantiates appropriate
     * subclass of VimCommand.
     * @param args An array of Strings as arguments
     * @return VimCommand, which is either an ImageExpoter or ImageLoader instance based on the arguments
     * @throws ParseException
     */
    private VimCommand parseCommandLine(String[] args)
        throws ParseException
    {
        Options options = generateCommandLineOptions();

        try
        {
            CommandLineParser parser = new PosixParser();
            CommandLine commandLine = parser.parse(options, args);
            
            if( commandLine.hasOption("h") )
            {
                printCommandLineUsage(options);
                System.exit(0);
            }
            
            else if (commandLine.hasOption("export"))
            {
                if (commandLine.hasOption("e") == false
                        || commandLine.hasOption("c") == false)
                {
                    throw new MissingOptionException("ec");
                }
                
                String vimConfigFile = commandLine.getOptionValue("c");
                String exportConfigFile = commandLine.getOptionValue("e");
                
                VimConfig vimConfig = XmlBeanHelper.loadVimConfig(vimConfigFile);
                ExportConfig exportConfig = XmlBeanHelper.loadExportConfig(exportConfigFile);
                
                return new ImageExporter(vimConfig, exportConfig);
            }
            
            else if (commandLine.hasOption("import"))
            {
                if (commandLine.hasOption("i") == false
                        || commandLine.hasOption("m") == false
                        || commandLine.hasOption("c") == false)
                {
                    throw new MissingOptionException("ec");
                }
                
                String vimConfigFile = commandLine.getOptionValue("c");
                String manifestFile = commandLine.getOptionValue("m");
                String importConfigFile = commandLine.getOptionValue("i");
                
                VimConfig vimConfig = XmlBeanHelper.loadVimConfig(vimConfigFile);
                Manifest manifest = XmlBeanHelper.loadManifest(manifestFile);
                ImportConfig importConfig = XmlBeanHelper.loadImportConfig(importConfigFile);
                
                return new ImageLoader(vimConfig, manifest, importConfig);
            }
            
            else
            {
                throw new MissingOptionException("import or export");
            }
        }
        catch (MissingOptionException e)
        {
            e.printStackTrace();
            printCommandLineUsage(options);
            System.exit(0);
        }
        
        return null;
    }
    
    /**
     * Generates command line Options. 
     * @return Options Contains one or more Options and/or OptionGroup objects
     */
    private Options generateCommandLineOptions()
    {
        Options options = new Options();
        Option optHelp = new Option("h", "help", false, "print this message" );
        options.addOption(optHelp);

        OptionGroup group = new OptionGroup();
        group.setRequired(false);

        Option optExport = new Option("e", "export", true, "Export using the given file for the config file");
        optExport.setArgName("export config file");
        optExport.setRequired(false);
        group.addOption(optExport);

        Option optImport = new Option("i", "import", true, "Import using the given file for the config file");
        optImport.setArgName("import config file");
        optImport.setRequired(false);
        group.addOption(optImport);

        options.addOptionGroup(group);
        
        Option optManifestFile = new Option("m", "manifest", true, "Use given file for the manifest file");
        optManifestFile.setArgName("manifest file");
        optManifestFile.setRequired(false);
        options.addOption(optManifestFile);

        Option optVimConfigFile = new Option("c", "config", true, "Use given file for the VIM configuration file");
        optVimConfigFile.setArgName("vim config file");
        optVimConfigFile.setRequired(false);
        options.addOption(optVimConfigFile);

        return options;
    }
    
    /**
     * This method prints correct usage of command line arguments
     * @param options Options object containing command line options.
     */
    private void printCommandLineUsage(Options options)
    {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setWidth(80);
        helpFormatter.setLeftPadding(2);
        helpFormatter.printHelp(USAGE, HEADER, options, FOOTER);
    }
}
