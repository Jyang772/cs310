eq <- function(x){log10(x)}
plot(eq,1,100,ylim=c(0,10),xlab="Elements",ylab="Operations")

lines(1:100,log2(1:100),col="red")
lines(1:100,log10((1:100)^2),col="blue")

my.expressions <- c(as.expression(bquote('log'['2']*'x')),as.expression(bquote('log'['10']*'x'^2)),as.expression(bquote('log'['10']*'x')))

#my.expressions <- c(as.expression(bquote('log'['10']*'x')),as.expression(bquote('log'['2']*'x')),as.expression(bquote('log'['10']*'x'^2)))

legend("topright",legend=my.expressions,lty=c(1,1),lwd=c(2.5,2.5,2.5),col=c("red","blue","black"))
