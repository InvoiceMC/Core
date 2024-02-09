package me.outspending.core.math

import org.bukkit.Location
import org.bukkit.util.Vector
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

object VectorUtils {
    private val EPSILON: Double = Math.ulp(1.0) * 2.0

    private fun epsilonCheck(
        a: Double,
        b: Double,
    ): Boolean {
        return abs(b - a) <= EPSILON
    }

    @JvmStatic
    fun rotateVector(
        vector: Vector,
        originVector: Vector,
    ): Vector = rotateVector(vector, originVector.x, originVector.y, originVector.z)

    @JvmStatic
    fun rotateAroundAxisX(
        vector: Vector,
        angle: Double,
    ): Vector {
        val cos = cos(angle)
        val sin = sin(angle)

        return Vector(
            vector.x,
            vector.y * cos - vector.z * sin,
            vector.y * sin + vector.z * cos,
        )
    }

    @JvmStatic
    fun rotateAroundAxisY(
        vector: Vector,
        angle: Double,
    ): Vector {
        val cos = cos(angle)
        val sin = sin(angle)

        return Vector(
            vector.x * cos + vector.z * sin,
            vector.y,
            -vector.x * sin + vector.z * cos,
        )
    }

    @JvmStatic
    fun rotateAroundAxisZ(
        vector: Vector,
        angle: Double,
    ): Vector {
        val cos = cos(angle)
        val sin = sin(angle)

        return Vector(
            vector.x * cos - vector.y * sin,
            vector.x * sin + vector.y * cos,
            vector.z,
        )
    }

    @JvmStatic
    fun rotateVector(
        vector: Vector,
        angleX: Double,
        angleY: Double,
        angleZ: Double,
    ): Vector {
        return rotateAroundAxisZ(
            rotateAroundAxisY(
                rotateAroundAxisX(vector, angleX),
                angleY,
            ),
            angleZ,
        )
    }

    @JvmStatic
    fun dotProduct(
        vector1: Vector,
        vector2: Vector,
    ): Double {
        return vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z
    }

    @JvmStatic
    fun crossProduct(
        vector1: Vector,
        vector2: Vector,
    ): Vector {
        val x = vector1.y * vector2.z - vector1.z * vector2.y
        val y = vector1.z * vector2.x - vector1.x * vector2.z
        val z = vector1.x * vector2.y - vector1.y * vector2.x

        return Vector(x, y, z)
    }

    @JvmStatic
    fun rotateAroundVector(
        location: Location,
        vector: Vector,
    ): Location {
        val (vx, vy, vz) = Vector3D(vector)

        val zeroZ = epsilonCheck(vz, 0.0)
        return if (zeroZ && epsilonCheck(vx, 0.0)) {
            location.add(0.0, vy, 0.0)
        } else {
            val yaw = location.yaw
            val yawRad = Math.toRadians(yaw.toDouble())

            val forward =
                Vector(
                    -sin(yawRad),
                    0.0,
                    cos(yawRad),
                )

            val forwardMultiplier = forward.multiply(vx)
            if (zeroZ) {
                location.add(forwardMultiplier)
            } else {
                val right = Vector(forward.z, 0.0, -forward.x)
                location.add(forwardMultiplier).add(right.multiply(vz))
            }
        }
    }

    private data class Vector3D(val vx: Double, val vy: Double, val vz: Double) {
        constructor(vector: Vector) : this(vector.x, vector.y, vector.z)
    }
}
