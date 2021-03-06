package dk.statsbiblioteket.broadcasttranscoder.processors;

import dk.statsbiblioteket.broadcasttranscoder.cli.InfrastructureContext;
import dk.statsbiblioteket.broadcasttranscoder.cli.SingleTranscodingContext;
import dk.statsbiblioteket.broadcasttranscoder.util.FileUtils;
import dk.statsbiblioteket.broadcasttranscoder.util.MetadataUtils;
import dk.statsbiblioteket.broadcasttranscoder.persistence.dao.HibernateUtil;
import dk.statsbiblioteket.broadcasttranscoder.persistence.dao.ProgramMediaInfoDAO;
import dk.statsbiblioteket.broadcasttranscoder.persistence.BroadcastTypeEnum;
import dk.statsbiblioteket.broadcasttranscoder.persistence.entities.ProgramMediaInfo;
import dk.statsbiblioteket.broadcasttranscoder.persistence.MediaTypeEnum;

import java.io.File;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: csr
 * Date: 10/1/12
 * Time: 2:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class TranscoderPersistenceProcessor extends ProcessorChainElement {


    @Override
    protected void processThis(TranscodeRequest request, SingleTranscodingContext context) throws ProcessorException {
        ProgramMediaInfo info = new ProgramMediaInfo();
        switch (request.getFileFormat()) {
            case MULTI_PROGRAM_MUX:
                info.setBroadcastType(BroadcastTypeEnum.TV);
                break;
            case MPEG_PS:
                info.setBroadcastType(BroadcastTypeEnum.TV);
                break;
            case SINGLE_PROGRAM_VIDEO_TS:
                 info.setBroadcastType(BroadcastTypeEnum.TV);
                break;
            case AUDIO_WAV:
                 info.setBroadcastType(BroadcastTypeEnum.RADIO);
                break;
            case SINGLE_PROGRAM_AUDIO_TS:
                  info.setBroadcastType(BroadcastTypeEnum.RADIO);
                break;
        }
        info.setEndOffset(request.getEndOffsetUsed());
        info.setStartOffset(request.getStartOffsetUsed());
        final File mediaOutputFile = FileUtils.getFinalMediaOutputFile(request, context);
        info.setFileExists(mediaOutputFile.exists());
        info.setFileSizeByte(mediaOutputFile.length());
        long programLength = MetadataUtils.findProgramLengthMillis(request)/1000  - request.getStartOffsetUsed() + request.getEndOffsetUsed();
        info.setExpectedFileSizeByte(request.getBitrate()*programLength);
        info.setFileTimestamp(new Date(mediaOutputFile.lastModified()));
        info.setLengthInSeconds((int) programLength);
        if (mediaOutputFile.getAbsolutePath().endsWith("mp3")) {
            info.setMediaType(MediaTypeEnum.MP3);
        } else if (mediaOutputFile.getAbsolutePath().endsWith("flv")) {
            info.setMediaType(MediaTypeEnum.FLV);
        }
        info.setTranscodeCommandLine(request.getTranscoderCommand());
        info.setShardUuid(request.getObjectPid());
        info.setNote("shardUuid is actually programUuid");
        new ProgramMediaInfoDAO(HibernateUtil.getInstance(context.getHibernateConfigFile().getAbsolutePath())).create(info);
    }
}
