正如Netty官网所描述的：
    Netty is an asynchronous event-driven network application framework 
    for rapid development of maintainable high performance protocol servers & clients.
所以只要涉及到服务间的通信并且BIO无法满足性能要求的情况下都可使用Netty框架，
所以其应用场景有：RPC、高并发、消息中间件、web服务、注册中心和调度系统等