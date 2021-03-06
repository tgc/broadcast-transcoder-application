/* $Id$
 * $Revision$
 * $Date$
 * $Author$
 *
 *
 */
package dk.statsbiblioteket.broadcasttranscoder.processors;

import dk.statsbiblioteket.broadcasttranscoder.cli.SingleTranscodingContext;
import dk.statsbiblioteket.broadcasttranscoder.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class UnistreamVideoTranscoderProcessor extends ProcessorChainElement {

    private static Logger log = LoggerFactory.getLogger(UnistreamVideoTranscoderProcessor.class);

    public UnistreamVideoTranscoderProcessor() {
    }

    public UnistreamVideoTranscoderProcessor(ProcessorChainElement childElement) {
        super(childElement);
    }

    @Override
    protected void processThis(TranscodeRequest request, SingleTranscodingContext context) throws ProcessorException {
        mpegClip(request, context);
    }

    private void mpegClip(TranscodeRequest request, SingleTranscodingContext context) throws ProcessorException {
        String command = "cat " + request.getClipperCommand() + " | " + getFfmpegCommandLine(request, context);
        File outputDir = FileUtils.getTemporaryMediaOutputDir(request, context);
        outputDir.mkdirs();
        File outputFile = FileUtils.getTemporaryMediaOutputFile(request, context);
        try {
            long timeout;
            if (request.getTimeoutMilliseconds() == 0l) {
                timeout = MetadataUtils.getTimeout(request, context);
            } else if (request.getFfprobeDurationSeconds() != null) {
                timeout = (long) (Math.round(request.getFfprobeDurationSeconds()*1000L
                        - request.getStartOffsetUsed()*1000L
                        + request.getEndOffsetUsed()*1000L
                )/context.getTranscodingTimeoutDivisor());
            } else {
                timeout = request.getTimeoutMilliseconds();
            }
            log.debug("Setting transcoding timeout for '" + request.getObjectPid() + "' to " + timeout + "ms");
            request.setTranscoderCommand(command);
            ExternalJobRunner.runClipperCommand(timeout, command);
        } catch (ExternalProcessTimedOutException e) {
            FileUtils.deleteAndLogFailedFile(outputFile, e);
            throw new ProcessorException("External process timed out for " + request.getObjectPid(),e);
        }
    }

    public static String getFfmpegCommandLine(TranscodeRequest request, SingleTranscodingContext context) {
        File outputFile = FileUtils.getTemporaryMediaOutputFile(request, context);
        String line = context.getFfmpegTranscodingString();
        if (request.getFileFormat().equals(FileFormatEnum.MPEG_PS) && context.getVideoOutputSuffix().equals("mpeg")) {
            line = context.getVlcRemuxingString();
        }
        line = line.replace("$$AUDIO_BITRATE$$", context.getAudioBitrate()+"");
        line = line.replace("$$VIDEO_BITRATE$$", context.getVideoBitrate()+"");
        line = line.replace("$$FFMPEG_ASPECT_RATIO$$", getFfmpegAspectRatio(request, context));
        line = line.replace("$$OUTPUT_FILE$$", outputFile.getAbsolutePath());
        return line;
    }


    protected static String getFfmpegAspectRatio(TranscodeRequest request, SingleTranscodingContext context) {
        Double aspectRatio = request.getDisplayAspectRatio();
        String ffmpegResolution;
        Long height = context.getVideoHeight()*1L;
        if (aspectRatio != null) {
            long width = 2*Math.round(aspectRatio*height/2);
            //if (width%2 == 1) width += 1;
            ffmpegResolution =  width + "x" + height;
        } else {
            ffmpegResolution = " 320x240";
        }
        return ffmpegResolution;
    }

}
