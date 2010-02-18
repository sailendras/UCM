import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

public class Replicator {

	public String sourceImagePath;
	public String destinationDirPath;
	public int cloneCount;
	public File newImageFile;
	public File destinationDir;
	
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	
	private static final String USAGE = "java -jar Replicator.jar [-h]\n" + 
    "-s <path to source image> -d <destination dir for replica images. Note trailing slash> -c <number of replicas to make> ";
    
	
	private static final String HEADER = "\n\nOptions:";

	private static final String FOOTER = "\n\nExamples:\n\n" +
	"Example 1:  java -jar Replicator.jar -s /opt/images/image1.jpeg -d /opt/clonedImages/ -c 300  \n\n\n" +
	"Example 2:  java -jar Replicator.jar -s C:\\images\\image1.jpeg -d C:\\clonedImages\\ -c 300  \n\n\n" ;

	public static void main(String[] args)
	{
		Replicator replicator = new Replicator();
		replicator.execute(args);
	}
	
	private boolean deleteDir(File dirToDelete)
	{
		FilenameFilter logFilter = new FilenameFilter() {
	        public boolean accept(File dir, String name) {
	            return !name.endsWith(".log");
	        }
	    };
	    
        if (dirToDelete.isDirectory()) {
        	System.out.println("Deleting this directory and its children");
            String[] children = dirToDelete.list(logFilter);
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dirToDelete, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        // The directory is now empty so delete it
        return dirToDelete.delete();
	}
	
	private void execute(String[] args)
	{
		
		Options options = generateCommandLineOptions();
		try
        {
            long startTime = System.currentTimeMillis();
            
            CommandLineParser parser = new PosixParser();
	        CommandLine commandLine = parser.parse(options, args);
	        
	        // Make sure user enters all command line options
	        if( commandLine.hasOption("s") == false || commandLine.hasOption("d") == false || commandLine.hasOption("c") == false )
	        {
	            printCommandLineUsage(options);
	            System.exit(0);
	        }
	        // If he enters all required parameters, get those values
	        else
	        {
	            this.sourceImagePath = commandLine.getOptionValue("s") + FILE_SEPARATOR;
	            this.destinationDirPath = commandLine.getOptionValue("d") + FILE_SEPARATOR;
	            this.cloneCount = Integer.parseInt(commandLine.getOptionValue("c"));
	        }
	        
	        // Create a file from the source directory. This will be the seed for cloning images
    	    File sourceImageFile = new File(this.sourceImagePath);
            
    	    // This will ensure all sub directories are cleaned and created afresh
    	    //deleteDir(new File(this.destinationDirPath)); //TODO add only when u have replicatedimages sub dir
    	    new File(this.destinationDirPath).mkdirs(); // Make sure all dirs exist
    	    
    	    // Create a date formatter to use it for unique file names
    	    Date todaysDate = new java.util.Date();
        	SimpleDateFormat formatter = new SimpleDateFormat("EEEddMMMyyyy_HH_mm_ss");
        	String formattedDate = formatter.format(todaysDate);
        	
    	    // Iterate over the clone count number and create clones
            for(int currentIteration = 0; currentIteration < this.cloneCount; currentIteration++)
            {
            	InputStream in = new FileInputStream(sourceImageFile);
            	
            	String newImageName = currentIteration + formattedDate + ".jpg";
            	String newImagePath = this.destinationDirPath + newImageName;
            	
            	newImageFile = new File(newImagePath);
            	OutputStream out = new FileOutputStream(newImageFile);
            	
            	byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0)
                {
                	out.write(buf, 0, len);
                }
                
                System.out.println("Cloning completed using imagename :" + newImageName);
                out.close();
                in.close();
            }
            
            long endTime = System.currentTimeMillis();
            System.out.println("Total processing time = " + (endTime - startTime));
    	}
            
	    catch (MissingOptionException e)
	    {
	        e.printStackTrace();
	        printCommandLineUsage(options);
	        System.exit(0);
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	}
	
	private Options generateCommandLineOptions()
    {
        Options options = new Options();
        
        Option optHelp = new Option("h", "help", false, "print this message" );
        options.addOption(optHelp);


        Option optSourceImage = new Option("s", "sourceImage", true, "Use this image as source to replicate");
        optSourceImage.setArgName("Source image file");
        optSourceImage.setRequired(true);
        options.addOption(optSourceImage);

        Option destDir = new Option("d", "destinationDir", true, "Use this directory to place all cloned images");
        destDir.setArgName("Destination Dir");
        destDir.setRequired(true);
        options.addOption(destDir);

        Option replicaCount = new Option("c", "replicaCount", true, "Use this number for replica count");
        replicaCount.setArgName("manifest file");
        replicaCount.setRequired(true);
        options.addOption(replicaCount);
        
        return options;
    }
    
    private static void printCommandLineUsage(Options options)
    {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setWidth(80);
        helpFormatter.setLeftPadding(2);
        helpFormatter.printHelp(USAGE, HEADER, options, FOOTER);
    }

	public String getSourceImagePath() {
		return sourceImagePath;
	}

	public void setSourceImagePath(String sourceImagePath) {
		this.sourceImagePath = sourceImagePath;
	}

	public String getDestinationDirPath() {
		return destinationDirPath;
	}

	public void setDestinationDirPath(String destinationDirPath) {
		this.destinationDirPath = destinationDirPath;
	}

	public int getCloneCount() {
		return cloneCount;
	}

	public void setCloneCount(int cloneCount) {
		this.cloneCount = cloneCount;
	}
    
    
}