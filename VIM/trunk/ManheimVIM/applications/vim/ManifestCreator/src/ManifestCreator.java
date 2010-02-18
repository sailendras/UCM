import java.io.File;
import java.io.FilenameFilter;
import java.util.Random;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlOptions;

import com.manheim.vim.schemas.config.manifest.ManifestDocument;
import com.manheim.vim.schemas.config.manifest.MetadataEntry;
import com.manheim.vim.schemas.config.manifest.ManifestDocument.Manifest;
import com.manheim.vim.schemas.config.metadata.Metadata;
import com.manheim.vim.schemas.config.vim.VimConfigDocument.VimConfig;
import com.manheim.vim.schemas.config.ximport.ImportConfigDocument.ImportConfig;


public class ManifestCreator {

	public String imagesSourceDirPath;
	public String manifestDestinationDirPath;
	public String importCongifPath;
	public String vimConfigPath;
	public MetadataEntry metadataEntry;
	public Manifest manifest;
	public ManifestDocument manifestDocument;
	
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	
	private static final String USAGE = "java -jar ManifestCreator.jar [-h]\n" + 
    "-s <path to source images> -d <destination dir for manifest file. Note trailing slash> +" +
    "-i <path to import configuration file> -c <Path to vim configuration file>";
	
	private static final String HEADER = "\n\nOptions:";

	private static final String FOOTER = "\n\nExamples:\n\n" +
	"Example 1:  java -jar ManifestCreator.jar -s /opt/replicatedImages/ -d /opt/  -i /opt/import-config.xml -c /opt/vim-config.xml \n\n\n" +
	"Example 2:  java -jar ManifestCreator.jar -s C:\\replicatedImages\\ -d C:\\  -i C:\\import-config.xml -c C:\\vim-config.xml \n\n\n" ;
	
	public static void main(String[] args) 
	{
		ManifestCreator manifestCreatorTest = new ManifestCreator();
		manifestCreatorTest.execute(args);
	}
	
	public void execute(String[] args)
	{
		Options options = generateCommandLineOptions();
		try 
		{
			Random rand = new Random();// used for woNum
			
			long startTime = System.currentTimeMillis();
            CommandLineParser parser = new PosixParser();
	        CommandLine commandLine = parser.parse(options, args);
	        
	        // Make sure user enters all command line options
	        if( commandLine.hasOption("s") == false || commandLine.hasOption("d") == false ||
	        		commandLine.hasOption("i") == false || commandLine.hasOption("c") == false)
	        {
	            printCommandLineUsage(options);
	            System.exit(0);
	        }
	        // If he enters all required parameters, get those values
	        else
	        {
	            this.imagesSourceDirPath = commandLine.getOptionValue("s") + FILE_SEPARATOR;
	            this.manifestDestinationDirPath = commandLine.getOptionValue("d") + FILE_SEPARATOR;
	            this.importCongifPath = commandLine.getOptionValue("i");
	            this.vimConfigPath = commandLine.getOptionValue("c");
	        }
	        
            VimConfig vimConfig = XmlBeanHelper.loadVimConfig(vimConfigPath);
            ImportConfig importConfig = XmlBeanHelper.loadImportConfig(importCongifPath);
            
			// Do not create a manifest entry any files other than images
			FilenameFilter filter = new FilenameFilter() {
		        public boolean accept(File dir, String name) {
		            return !name.endsWith(".sh") && !name.endsWith(".jar") && !name.endsWith(".db") && !name.endsWith(".log") && !name.endsWith(".xml") && !name.endsWith(".nfs*");
		        }
		    };
		    File[] listOfFiles = new File(imagesSourceDirPath).listFiles(filter);
		    
	        manifestDocument = ManifestDocument.Factory.newInstance();
	        manifest = manifestDocument.addNewManifest();
	        
	        // Setting common attributes specified in vim-config.xml and import-config.xml files to each manifest entry
			for(int currentIteration = 0; currentIteration < listOfFiles.length; currentIteration++)
			{
				metadataEntry = manifest.addNewMetadataEntries();
				
				metadataEntry.setImageName(listOfFiles[currentIteration].getName());
				addMetadataElement("xComments", StringUtils.split(listOfFiles[currentIteration].getName(), ".")[0]);
				
				for (Metadata defaultAttribute: vimConfig.getDefaultAttributesList())
				{
					addMetadataElement(defaultAttribute.getName(), defaultAttribute.getValue());
				}
				for (Metadata defaultAttribute: importConfig.getDefaultAttributesList())
				{
					addMetadataElement(defaultAttribute.getName(), defaultAttribute.getValue());
				}
				//TODO for testing purposes, we need to add some attributes to the manifest file (needed for INSERT_URL_PARING_ROW service)
				addMetadataElement("xAuctionId", "AUC_02");
				addMetadataElement("xImageSequence", String.valueOf(rand.nextInt(999)));
				addMetadataElement("xWorkOrderNumber", rand.nextInt(9) + DateHelper.getUniqueDateStr());
				
			}
			
            // generate the manifest file
            File file = new File(this.manifestDestinationDirPath + "manifest.xml");
            XmlOptions opts = new XmlOptions();
            opts.setSavePrettyPrint();
            opts.setSavePrettyPrintIndent(4);
            manifestDocument.save(file, opts);
            
	        long endTime = System.currentTimeMillis();
            System.out.println("Total processing time = " + (endTime - startTime));
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	private void addMetadataElement(String name, String value)
	{
		Metadata metadata = metadataEntry.addNewMetadata();
		metadata.setName(name);
		metadata.setValue(value);
	}
	
	private Options generateCommandLineOptions()
    {
        Options options = new Options();
        
        Option optHelp = new Option("h", "help", false, "print this message" );
        options.addOption(optHelp);

        Option optimageSourceDir = new Option("s", "imageSourceDir", true, "Use this dir for images source dir");
        optimageSourceDir.setArgName("Images source dir");
        optimageSourceDir.setRequired(true);
        options.addOption(optimageSourceDir);

        Option manifestDestDir = new Option("d", "manifestDestinationDir", true, "Use this directory to place manifest file");
        manifestDestDir.setArgName("Manifest destination dir");
        manifestDestDir.setRequired(true);
        options.addOption(manifestDestDir);
        
        Option optImport = new Option("i", "import", true, "Import using the given file for the config file");
        optImport.setArgName("import config file");
        optImport.setRequired(false);
        options.addOption(optImport);
        
        Option optVimConfigFile = new Option("c", "config", true, "Use given file for the VIM configuration file");
        optVimConfigFile.setArgName("vim config file");
        optVimConfigFile.setRequired(false);
        options.addOption(optVimConfigFile);
        
        return options;
    }
	
    private static void printCommandLineUsage(Options options)
    {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setWidth(80);
        helpFormatter.setLeftPadding(2);
        helpFormatter.printHelp(USAGE, HEADER, options, FOOTER);
    }
}
