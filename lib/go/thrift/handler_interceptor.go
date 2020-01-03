package thrift

import (
	"context"
)

type HandlerInterceptor func(ctx context.Context, arg interface{}, handlerFunc func(ctx context.Context, arg interface{}) (result interface{}, err error)) (result interface{}, err error)

func ChainedHandlerInterceptor(interceptors ...HandlerInterceptor) HandlerInterceptor {
	interceptorCount := len(interceptors)
	interceptorCount--

	chainer := func(currentInterceptor HandlerInterceptor, nextInterceptor HandlerInterceptor) HandlerInterceptor {
		return func(ctx context.Context, arg interface{}, handlerFunc func(ctx context.Context, arg interface{}) (interface{}, error)) (result interface{}, err error) {
			return currentInterceptor(ctx, arg, func(ctx context.Context, arg interface{}) (result interface{}, err error) {
				return nextInterceptor(ctx, arg, handlerFunc)
			})
		}
	}

	chainedHandlerInterceptor := interceptors[interceptorCount]

	for i := interceptorCount - 1; i >= 0; i-- {
		chainedHandlerInterceptor = chainer(chainedHandlerInterceptor, interceptors[i])
	}

	return chainedHandlerInterceptor
}
