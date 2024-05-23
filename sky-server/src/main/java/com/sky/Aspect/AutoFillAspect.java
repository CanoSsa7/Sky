package com.sky.Aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 *
 * @author CanoSsa7
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    //用方法定义一个切入点表达式
    @Pointcut("@annotation(com.sky.annotation.AutoFill)")
    void autoFillPointCut(){}
    @Before("autoFillPointCut()")
    void autoFill(JoinPoint joinPoint) throws NoSuchMethodException {
        log.info("开始公共字段自动填充");

        //获取当前方法的执行类型（SELECT或UPDATE）
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); //获取方法签名
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class); //通过反射，获取注解对象
        OperationType type = autoFill.value(); //获取注解对象的值，得到操作类型

        //获取方法参数，即实体对象
        Object[] objects = joinPoint.getArgs();
        if(objects == null || objects.length == 0){
            return;
        }
        Object entity = objects[0];

        //准备赋值数据
        LocalDateTime now = LocalDateTime.now(); //时间
        Long currentId = BaseContext.getCurrentId(); //操作id

        if(autoFill.value() == OperationType.UPDATE){
            //更新操作只用更改两个字段
            try{
                //通过反射获取实体类的方法，这里的方法名可以用常量字符串
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateId = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //执行方法
                setUpdateId.invoke(entity, currentId);
                setUpdateTime.invoke(entity, now);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(autoFill.value() == OperationType.INSERT){
            try{
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateId = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateId = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);

                //执行方法
                setUpdateId.invoke(entity, currentId);
                setUpdateTime.invoke(entity, now);
                setCreateId.invoke(entity, currentId);
                setCreateTime.invoke(entity, now);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
