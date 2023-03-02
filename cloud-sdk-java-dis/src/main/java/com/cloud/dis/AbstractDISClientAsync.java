package com.cloud.dis;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.cloud.dis.exception.DISClientException;
import com.cloud.dis.http.AbstractDISClient;
import com.cloud.dis.iface.app.request.ListAppsRequest;
import com.cloud.dis.iface.app.request.ListStreamConsumingStateRequest;
import com.cloud.dis.iface.app.response.DescribeAppResult;
import com.cloud.dis.iface.app.response.ListAppsResult;
import com.cloud.dis.iface.app.response.ListStreamConsumingStateResult;
import com.cloud.dis.iface.data.request.*;
import com.cloud.dis.iface.data.response.*;
import com.cloud.dis.iface.stream.request.*;
import com.cloud.dis.iface.stream.response.*;
import com.cloud.dis.iface.transfertask.request.*;
import com.cloud.dis.iface.transfertask.response.*;
import com.cloud.dis.iface.data.request.*;
import com.cloud.dis.iface.data.response.*;
import com.cloud.dis.iface.stream.request.*;
import com.cloud.dis.iface.stream.response.*;
import com.cloud.dis.iface.transfertask.request.*;
import com.cloud.dis.iface.transfertask.response.*;

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
    
    @Override
    public CreateTransferTaskResult createTransferTask(CreateTransferTaskRequest createTransferTaskRequest) {
        try {
            return createTransferTaskAsync(createTransferTaskRequest).get();
        } catch (ExecutionException e) {
            throw new DISClientException(e.getCause() == null ? e : e.getCause());
        }catch(InterruptedException e) {
            throw new DISClientException(e);
        }
    }
    
    @Override
    public UpdateTransferTaskResult updateTransferTask(UpdateTransferTaskRequest updateTransferTaskRequest) {
        try {
            return updateTransferTaskAsync(updateTransferTaskRequest).get();
        } catch (ExecutionException e) {
            throw new DISClientException(e.getCause() == null ? e : e.getCause());
        }catch(InterruptedException e) {
            throw new DISClientException(e);
        }
    }
    
    @Override
    public DeleteTransferTaskResult deleteTransferTask(DeleteTransferTaskRequest deleteTransferTaskRequest) {
        try {
            return deleteTransferTaskAsync(deleteTransferTaskRequest).get();
        } catch (ExecutionException e) {
            throw new DISClientException(e.getCause() == null ? e : e.getCause());
        }catch(InterruptedException e) {
            throw new DISClientException(e);
        }
    }
    
    @Override
    public ListTransferTasksResult listTransferTasks(ListTransferTasksRquest listTransferTasksRequest) {
        try {
            return listTransferTasksAsync(listTransferTasksRequest).get();
        } catch (ExecutionException e) {
            throw new DISClientException(e.getCause() == null ? e : e.getCause());
        }catch(InterruptedException e) {
            throw new DISClientException(e);
        }
    }
    
    @Override
    public DescribeTransferTaskResult describeTransferTask(DescribeTransferTaskRequest describeTransferTaskRequest) {
        try {
            return describeTransferTaskAsync(describeTransferTaskRequest).get();
        } catch (ExecutionException e) {
            throw new DISClientException(e.getCause() == null ? e : e.getCause());
        }catch(InterruptedException e) {
            throw new DISClientException(e);
        }
    }
}
