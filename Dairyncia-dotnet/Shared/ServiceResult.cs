public class ServiceResult<T> where T : class
{
    public bool IsSuccess { get; }
    public string? Error { get; }
    public T? Data { get; }

    private ServiceResult(bool success, T? data, string? error)
    {
        IsSuccess = success;
        Data = data;
        Error = error;
    }

    public static ServiceResult<T> Success(T data)
        => new(true, data, null);

    public static ServiceResult<T> Fail(string error)
        => new(false, null, error);
}
