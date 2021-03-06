package dk.statsbiblioteket.broadcasttranscoder.processors;

import dk.statsbiblioteket.broadcasttranscoder.cli.InfrastructureContext;
import dk.statsbiblioteket.broadcasttranscoder.cli.SingleTranscodingContext;

/**
 *
 */
public class TranscoderDispatcherProcessor extends ProcessorChainElement {

    public TranscoderDispatcherProcessor() {
    }

    public TranscoderDispatcherProcessor(ProcessorChainElement childElement) {
        super(childElement);
    }

    @Override
    protected void processThis(TranscodeRequest request, SingleTranscodingContext context) throws ProcessorException {
        switch (request.getFileFormat()) {
            case MULTI_PROGRAM_MUX:
                this.setChildElement(new PidAndAsepctRatioExtractorProcessor());
                break;
            case SINGLE_PROGRAM_VIDEO_TS:
                this.setChildElement(new PidAndAsepctRatioExtractorProcessor());
                break;
            case SINGLE_PROGRAM_AUDIO_TS:
                this.setChildElement(new PidAndAsepctRatioExtractorProcessor());
                break;
            case MPEG_PS:
                this.setChildElement(new PidAndAsepctRatioExtractorProcessor());
                break;
            case AUDIO_WAV:
                final WavTranscoderProcessor childElement1 = new WavTranscoderProcessor();
                this.setChildElement(childElement1);
                break;
        }
    }
}
