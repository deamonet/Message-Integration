def get_wechat_pid_list() -> list:
    """
    获取所有微信pid

    Returns
    -------
    list
        微信pid列表.

    """
    import psutil
    pid_list = []
    process_list = psutil.pids()
    for pid in process_list:
        try:
            if psutil.Process(pid).name() == 'WeChat.exe':
                pid_list.append(pid)
        except psutil.NoSuchProcess:
            pass
    return pid_list
