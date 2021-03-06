package com.dmycqq.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MessageList;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class Client extends ChannelInboundHandlerAdapter {
	private static SocketAddress socketAddress = new InetSocketAddress(8080);
	private static ClientChannelInitializer clientChannelInitializer = new ClientChannelInitializer();
	
	static class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
		protected void initChannel(SocketChannel ch) throws Exception {
			ch.pipeline().addLast(new Client());
		}
	}

	public static void main(String[] args) throws Exception {
		EventLoopGroup g = new NioEventLoopGroup(1);
		Bootstrap b = new Bootstrap();
		b.group(g);
		b.channel(NioSocketChannel.class);
		b.option(ChannelOption.TCP_NODELAY, true);
		b.handler(clientChannelInitializer);
		ChannelFuture f = b.connect(socketAddress).sync();
		f.channel().closeFuture().sync(); 	
		g.shutdownGracefully();
	}
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println(ctx.name() + "=channelRegistered");
	}
	
	private final ByteBuf buffer = Unpooled.wrappedBuffer("Hello".getBytes());
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(ctx.name() + "=channelActive");
		ctx.write(buffer);
		ctx.close();
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageList<Object> msgs) throws Exception {
		System.out.println(ctx.name() + "=messageReceived");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		System.out.println(ctx.name() + "=exceptionCaught");
		ctx.close();
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println(ctx.name() + "=channelUnregistered");
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(ctx.name() + "=channelInactive");
	}
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.out.println(ctx.name() + "=handlerAdded");
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println(ctx.name() + "=handlerRemoved");
	}
}
