package me.outspending.core.utils

import org.bukkit.entity.Display
import org.bukkit.util.Transformation
import org.bukkit.util.Vector
import org.joml.AxisAngle4f
import org.joml.Quaternionf
import org.joml.Vector3f

object InterpolationUtils {

    private fun setInterpolation(displayEntity: Display, delay: Int, duration: Int) {
        displayEntity.interpolationDelay = delay
        displayEntity.interpolationDuration = duration
    }

    @JvmStatic
    fun moveTo(displayEntity: Display, vector: Vector, delay: Int = -1, duration: Int = 20) {
        displayEntity.apply {
            setInterpolation(this, delay, duration)

            val transformation: Transformation = this.transformation
            val translation: Vector3f = transformation.translation

            translation.set(vector.x, vector.y, vector.z)

            this.transformation = transformation
        }
    }

    @JvmStatic
    fun rotateLeft(
        displayEntity: Display,
        axis: Vector,
        angle: Float,
        delay: Int = -1,
        duration: Int = 20
    ) {
        displayEntity.apply {
            setInterpolation(this, delay, duration)

            val transformation: Transformation = this.transformation
            val rotation: Quaternionf = transformation.leftRotation
            val floatValues = Double2Float(axis.x, axis.y, axis.z)

            rotation.set(AxisAngle4f(angle, floatValues.x, floatValues.y, floatValues.z))

            this.transformation = transformation
        }
    }

    @JvmStatic
    fun rotateRight(
        displayEntity: Display,
        axis: Vector,
        angle: Float,
        delay: Int = -1,
        duration: Int = 20
    ) {
        displayEntity.apply {
            setInterpolation(this, delay, duration)

            val transformation: Transformation = this.transformation
            val rotation: Quaternionf = transformation.rightRotation
            val floatValues = Double2Float(axis.x, axis.y, axis.z)

            rotation.set(AxisAngle4f(angle, floatValues.x, floatValues.y, floatValues.z))
            this.transformation = transformation
        }
    }

    @JvmStatic
    fun scale(displayEntity: Display, scale: Vector, delay: Int = -1, duration: Int = 20) {
        displayEntity.apply {
            setInterpolation(this, delay, duration)

            val transformation: Transformation = this.transformation
            val transformationScale: Vector3f = transformation.scale

            transformationScale.set(scale.x, scale.y, scale.z)

            this.transformation = transformation
        }
    }

    private data class Double2Float(val x: Float, val y: Float, val z: Float) {
        constructor(x: Double, y: Double, z: Double) : this(x.toFloat(), y.toFloat(), z.toFloat())
    }
}
