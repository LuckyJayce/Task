package com.shizhefei.task.demo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import rx.Observable;
import rx.observers.Observers;

public class Test {
    public static void main(String[] args) {
        final LinkedList<OnTest> list = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                final int finalI = i;
                list.add(new OnTest() {
                    @Override
                    public void onTest() {
                        System.out.println("remove " + finalI);
                        list.remove(this);
                    }
                });
            } else {
                final int finalI1 = i;
                list.add(new OnTest() {
                    @Override
                    public void onTest() {
                        System.out.println("print " + finalI1);
                    }
                });
            }
        }

        for (OnTest onTest : list) {
            onTest.onTest();
        }

        for (OnTest onTest : list) {
            onTest.onTest();
        }
//        Iterator<OnTest> iterator = list.iterator();
//        while (iterator.hasNext()){
//            iterator.next().onTest();
//        }
//        ArrayList<OnTest> onTests = new ArrayList<>(list);
//        for (OnTest onTest : onTests) {
//            onTest.onTest();
//        }

        Observable<LinkedList<OnTest>> just = Observable.just(list);
    }

    private interface OnTest {
        void onTest();
    }
}
