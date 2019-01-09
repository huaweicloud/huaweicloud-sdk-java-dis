package com.huaweicloud.dis;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.huaweicloud.dis.exception.DISClientException;
import com.huaweicloud.dis.http.AbstractDISClient;
import com.huaweicloud.dis.iface.app.request.ListAppsRequest;
import com.huaweicloud.dis.iface.app.request.ListStreamConsumingStateRequest;
import com.huaweicloud.dis.iface.app.response.DescribeAppResult;
import com.huaweicloud.dis.iface.app.response.ListAppsResult;
import com.huaweicloud.dis.iface.app.response.ListStreamConsumingStateResult;
import com.huaweicloud.dis.iface.data.request.CommitCheckpointRequest;
import com.huaweicloud.dis.iface.data.request.DeleteCheckpointRequest;
import com.huaweicloud.dis.iface.data.request.GetCheckpointRequest;
import com.huaweicloud.dis.iface.data.request.GetPartitionCursorRequest;
import com.huaweicloud.dis.iface.data.request.GetRecordsRequest;
import com.huaweicloud.dis.iface.data.request.PutRecordRequest;
import com.huaweicloud.dis.iface.data.request.PutRecordsRequest;
import com.huaweicloud.dis.iface.data.response.CommitCheckpointResult;
import com.huaweicloud.dis.iface.data.response.DeleteCheckpointResult;
import com.huaweicloud.dis.iface.data.response.GetCheckpointResult;
import com.huaweicloud.dis.iface.data.response.GetPartitionCursorResult;
import com.huaweicloud.dis.iface.data.response.GetRecordsResult;
import com.huaweicloud.dis.iface.data.response.PutRecordResult;
import com.huaweicloud.dis.iface.data.response.PutRecordsResult;
import com.huaweicloud.dis.iface.stream.request.CreateStreamRequest;
import com.huaweicloud.dis.iface.stream.request.DeleteStreamRequest;
import com.huaweicloud.dis.iface.stream.request.DescribeStreamRequest;
import com.huaweicloud.dis.iface.stream.request.ListStreamsRequest;
import com.huaweicloud.dis.iface.stream.request.UpdatePartitionCountRequest;
import com.huaweicloud.dis.iface.stream.request.UpdateStreamRequest;
import com.huaweicloud.dis.iface.stream.response.CreateStreamResult;
import com.huaweicloud.dis.iface.stream.response.DeleteStreamResult;
import com.huaweicloud.dis.iface.stream.response.DescribeStreamResult;
import com.huaweicloud.dis.iface.stream.response.ListStreamsResult;
import com.huaweicloud.dis.iface.stream.response.UpdatePartitionCountResult;
import com.huaweicloud.dis.iface.stream.response.UpdateStreamResult;

public abstract class AbstractDISClientAsync extends AbstractDISClient implements DIS, DISAsync{
	
	public AbstractDISClientAsync(DISConfig disConfig) {
		super(disConfig);
	}
	
	@Override
	public PutRecordsResult putRecords(PutRecordsRequest putRecordsParam) {
		try {
			return putRecordsAsync(putRecordsParam).get();
		} catch (ExecutionException e) {			
			throw new DISClientException(e.getCause() == null ? e : e.getCause());
		}catch(InterruptedException e) {
			throw new DISClientException(e);
		}
	}

	@Override
	public GetPartitionCursorResult getPartitionCursor(GetPartitionCursorRequest getShardIteratorParam) {
		try {
			return getPartitionCursorAsync(getShardIteratorParam).get();
		} catch (ExecutionException e) {			
			throw new DISClientException(e.getCause() == null ? e : e.getCause());
		}catch(InterruptedException e) {
			throw new DISClientException(e);
		}
	}

	@Override
	public GetRecordsResult getRecords(GetRecordsRequest getRecordsParam) {
		try {
			return getRecordsAsync(getRecordsParam).get();
		} catch (ExecutionException e) {			
			throw new DISClientException(e.getCause() == null ? e : e.getCause());
		}catch(InterruptedException e) {
			throw new DISClientException(e);
		}
	}

	@Override
	public CommitCheckpointResult commitCheckpoint(CommitCheckpointRequest commitCheckpointRequest) {
		try {
			return commitCheckpointAsync(commitCheckpointRequest).get();
		} catch (ExecutionException e) {			
			throw new DISClientException(e.getCause() == null ? e : e.getCause());
		}catch(InterruptedException e) {
			throw new DISClientException(e);
		}
	}

	@Override
	public GetCheckpointResult getCheckpoint(GetCheckpointRequest getCheckpointRequest) {
		try {
			return getCheckpointAsync(getCheckpointRequest).get();
		} catch (ExecutionException e) {			
			throw new DISClientException(e.getCause() == null ? e : e.getCause());
		}catch(InterruptedException e) {
			throw new DISClientException(e);
		}
	}

	@Override
	public CreateStreamResult createStream(CreateStreamRequest createStreamRequest) {
		try {
			return createStreamAsync(createStreamRequest).get();
		} catch (ExecutionException e) {			
			throw new DISClientException(e.getCause() == null ? e : e.getCause());
		}catch(InterruptedException e) {
			throw new DISClientException(e);
		}
	}

	@Override
	public DeleteStreamResult deleteStream(DeleteStreamRequest deleteStreamRequest) {
		try {
			return deleteStreamAsync(deleteStreamRequest).get();
		} catch (ExecutionException e) {			
			throw new DISClientException(e.getCause() == null ? e : e.getCause());
		}catch(InterruptedException e) {
			throw new DISClientException(e);
		}
	}

	@Override
	public ListStreamsResult listStreams(ListStreamsRequest listStreamsRequest) {
		try {
			return listStreamsAsync(listStreamsRequest).get();
		} catch (ExecutionException e) {			
			throw new DISClientException(e.getCause() == null ? e : e.getCause());
		}catch(InterruptedException e) {
			throw new DISClientException(e);
		}
	}

	@Override
	public DescribeStreamResult describeStream(DescribeStreamRequest describeStreamRequest) {
		try {
			return describeStreamAsync(describeStreamRequest).get();
		} catch (ExecutionException e) {			
			throw new DISClientException(e.getCause() == null ? e : e.getCause());
		}catch(InterruptedException e) {
			throw new DISClientException(e);
		}
	}

	@Override
	public UpdatePartitionCountResult updatePartitionCount(UpdatePartitionCountRequest updatePartitionCountRequest) {
		try {
			return updatePartitionCountAsync(updatePartitionCountRequest).get();
		} catch (ExecutionException e) {			
			throw new DISClientException(e.getCause() == null ? e : e.getCause());
		}catch(InterruptedException e) {
			throw new DISClientException(e);
		}
	}

	@Override
	public void createApp(String appName) {
		try {
			createAppAsync(appName).get();
		} catch (ExecutionException e) {			
			throw new DISClientException(e.getCause() == null ? e : e.getCause());
		}catch(InterruptedException e) {
			throw new DISClientException(e);
		}
	}

	@Override
	public void deleteApp(String appName) {
		try {
			deleteAppAsync(appName).get();
		} catch (ExecutionException e) {			
			throw new DISClientException(e.getCause() == null ? e : e.getCause());
		}catch(InterruptedException e) {
			throw new DISClientException(e);
		}
	}

	@Override
	public DescribeAppResult describeApp(String appName) {
		try {
			return describeAppAsync(appName).get();
		} catch (ExecutionException e) {			
			throw new DISClientException(e.getCause() == null ? e : e.getCause());
		}catch(InterruptedException e) {
			throw new DISClientException(e);
		}
	}

	@Override
	public ListAppsResult listApps(ListAppsRequest listAppsRequest) {
		try {
			return listAppsAsync(listAppsRequest).get();
		} catch (ExecutionException e) {			
			throw new DISClientException(e.getCause() == null ? e : e.getCause());
		}catch(InterruptedException e) {
			throw new DISClientException(e);
		}
	}

	@Override
	public PutRecordResult putRecord(PutRecordRequest putRecordParam) {
		try {
			return toPutRecordResult(putRecordsAsync(toPutRecordsRequest(putRecordParam)).get());
		} catch (ExecutionException e) {			
			throw new DISClientException(e.getCause() == null ? e : e.getCause());
		}catch(InterruptedException e) {
			throw new DISClientException(e);
		}
	}

	@Override
	public DeleteCheckpointResult deleteCheckpoint(DeleteCheckpointRequest deleteCheckpointRequest) {
		try {
			return deleteCheckpointAsync(deleteCheckpointRequest).get();
		} catch (ExecutionException e) {			
			throw new DISClientException(e.getCause() == null ? e : e.getCause());
		}catch(InterruptedException e) {
			throw new DISClientException(e);
		}
	}

	@Override
	public ListStreamConsumingStateResult listStreamConsumingState(ListStreamConsumingStateRequest listStreamConsumingStateRequest) {
		try {
			return listStreamConsumingStateAsync(listStreamConsumingStateRequest).get();
		} catch (ExecutionException e) {			
			throw new DISClientException(e.getCause() == null ? e : e.getCause());
		}catch(InterruptedException e) {
			throw new DISClientException(e);
		}
	}
	
	@Override
	public UpdateStreamResult updateStream(UpdateStreamRequest updateStreamRequest)
	{
	    try {
            return updateStreamAsync(updateStreamRequest).get();
        } catch (ExecutionException e) {            
            throw new DISClientException(e.getCause() == null ? e : e.getCause());
        }catch(InterruptedException e) {
            throw new DISClientException(e);
        }
	}

    private Future<UpdateStreamResult> updateStreamAsync(UpdateStreamRequest updateStreamRequest)
    {
        throw new UnsupportedOperationException();
    }
	
}
